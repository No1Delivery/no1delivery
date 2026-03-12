package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.domain.payment.domain.event.PaymentApprovedEvent;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentCancelFailedEvent;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentCanceledEvent;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentFailedEvent;
import com.sparta.no1delivery.domain.payment.domain.exception.PaymentApproveFailureException;
import com.sparta.no1delivery.domain.payment.domain.service.PaymentClientDto;
import com.sparta.no1delivery.domain.payment.domain.service.PaymentClient;
import com.sparta.no1delivery.global.infrastructure.event.Events;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 결제 요구사항
 * 1. 주문서가 주문 접수 상태로 변경되면 OrderAcceptEvent 발생
 * 2. OrderAcceptEvent 이벤트를 처리하는 핸들러 결제 등록
 * 3. 프론트 엔드에서 orderId(주문번호), OrderName(주문상품), amount(결제 금액)으로 결제 진행
 * 4. 성공 콜백으로 백엔드 엔드포인트로 paymentKey,orderId,amount로 넘어옴
 * 5. 백엔드 엔드포인트에서는 승인 처리를 하고 성공시 approve처리, 실패시 abort 처리
 *  - 승인 시간, paymentKey, status, paymentLog등을 업데이트 합니다
 * 6. 결제가 승인되면 주문서는 입금확인 단계로 업데이트
 * 7. 결제는 카드결제만 되며, 이후 확장성을 위해 다른 것들도 만들어 놓을 생각.
 */
@NoArgsConstructor
@Entity
@Getter
@Table(name = "P_Payment")
public class Payment {
    // paymentId 클래스로 id값 받기// 식별자
    @EmbeddedId
    private PaymentId id;

    // paymentKey 토스에서 결제요청을 보내면 해당 주문의 결제 요청이 성공되면 반환받는 key값 담을 공간
    @Column(length = 50, name = "payment_key")
    private String key;

    // payment 결제 수단 공간
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    // payment 결제 금액 공간
    @Embedded
    private PaymentAmount amount;

    // 결제의 상태를 확인하기 위한 필드
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    //결제 로그 담는 공간
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_log")
    private List<PaymentLog> logs = new ArrayList<>();

    // payment 주문결제상세 내용
    @Embedded
    private PaymentOrderInfo paymentInfo;

    //결제 요청 시간
    public LocalDateTime requestedAt;

    // 결제 승인 시간
    private LocalDateTime approvedAt;

    private LocalDateTime canceledAt;

    //생성자 기본 (orderId와 orderName, amount의 값을 받아 새 Payment 객체 인스턴스를 만듬
    @Builder
    public Payment(UUID orderId, String orderName, Long amount){
        this.id = PaymentId.of();
        this.status = PaymentStatus.READY;
        this.paymentInfo = new PaymentOrderInfo(orderId,orderName);
        this.requestedAt = LocalDateTime.now();
        this.amount = new PaymentAmount(amount);
    }

    // 결제 승인
    // 승인이 되려면 paymentKey와 orderId, amount가 필요 / 승인 처리가 되기 위해 상태가 필요/ 처리받는 결과를 담기 위한 log와 시간을 등록
    public void approve(String key, PaymentClient paymentClient){

        this.status.verifyNotProcessed();
        if (!StringUtils.hasText(key)) {
            throw new CustomException(ErrorCode.REQUIRED_PAYMENT_KEY);
        }

        // 결제 승인 요청
        PaymentClientDto result = paymentClient.requestApprove(key, paymentInfo.getOrderId(), amount.getValue());
        if (!result.success()) {
            this.abort(result.reason());
            throw new PaymentApproveFailureException(result.reason());
        }

        this.key = result.key();
        this.logs.add(new PaymentLog(LocalDateTime.now(), result.paymentLog()));

        // 결제 금액 위변조 체크
        if (amount.getValue() != result.approvedAmount()) { // 변조가 된 경우는 결제 취소
            paymentClient.requestCancel(id, key, "실결제 금액과 최초 등록 금액 불일치");
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        this.approvedAt = result.approvedAt() == null ? LocalDateTime.now() : result.approvedAt();
        this.status = PaymentStatus.DONE;

        Events.trigger(new PaymentApprovedEvent(paymentInfo.getOrderId()));
    }

    // 결제 취소
    public void cancel(String reason, PaymentClient paymentClient){
        if (this.status == PaymentStatus.CANCELLED) {
            return;
        }

        this.status.verifyCancelable();

        PaymentClientDto result = paymentClient.requestCancel(this.id, this.key, reason);
        if (!result.success()) {
            this.failCancel(result.reason());
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }

        this.status = PaymentStatus.CANCELLED;
        this.logs.add(new PaymentLog(LocalDateTime.now(), result.paymentLog()));
        this.canceledAt = LocalDateTime.now();

        // 주문 도메인에 환불 완료를 알립니다.
        Events.trigger(new PaymentCanceledEvent(this.paymentInfo.getOrderId(), this.amount.getValue()));
    }

    //결제 실패
    public void abort(String reason){
        this.status.verifyAbortable();
        status = PaymentStatus.ABORTED;

        Events.trigger(new PaymentFailedEvent(this.paymentInfo.getOrderId(), reason));
    }

    // 결제 취소 실패 (PG사 거절 등) [cite: 2026-03-05]
    public void failCancel(String reason){

        // [중요 추가] 관리자나 주문 도메인에 취소 실패를 알립니다.
        Events.trigger(new PaymentCancelFailedEvent(this.paymentInfo.getOrderId(), "CANCEL_ERROR", reason));
    }

}
