package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.domain.payment.domain.event.PaymentApprovedEvent;
import com.sparta.no1delivery.global.infrastructure.event.Events;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @Column(name = "payment_log", columnDefinition = "jsonb")
    private String paymentLog;

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
    public void approve(String key, LocalDateTime approvedAt, String paymentLog,Long approveAmount){
        this.status.verifyNotProcessed();
        this.amount.verifyAmount(approveAmount);
        this.key = key;
        this.status = PaymentStatus.DONE;
        this.approvedAt = approvedAt;
        this.paymentLog = paymentLog;

        Events.trigger(new PaymentApprovedEvent(paymentInfo.getOrderId()));
    }

    // 결제 취소
    public void cancel(String paymentLog, LocalDateTime canceledAt){
        this.status.verifyCancelable();
        this.status = PaymentStatus.CANCELLED;
        this.canceledAt = canceledAt;
        this.paymentLog = "%s\n[취소 요청 기록]:%s\n------------------------------------------------------".formatted(this.paymentLog, paymentLog);
    }

    //결제 실패
    public void abort(String paymentLog){
        this.status.verifyAbortable();
        status = PaymentStatus.ABORTED;
        this.paymentLog = "%s\n[결제 요청 실패 기록]:%s\n------------------------------------------------------".formatted(this.paymentLog, paymentLog);
    }

    public void failCancel(String failureLog){
        this.paymentLog = "%s\n[취소 실패 기록]: %s\n------------------------------------------------------".formatted(this.paymentLog, failureLog);
    }

}
