package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
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

    @EmbeddedId // paymentId 클래스로 id값 받기// 식별자
    private PaymentId id;

    @Column(length = 50, name = "payment_key") // paymentKey 토스에서 결제요청을 보내면 해당 주문의 결제 요청이 성공되면 반환받는 key값 담을 공간
    private String key;

    @Column(length = 30) // payment 결제 수단 공간
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Embedded // payment 결제 금액 공간
    private PaymentAmount amount;

    @Column(length = 30)// 결제의 상태를 확인하기 위한 필드
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payment_log", columnDefinition = "jsonb") //결제 로그 담는 공간
    private String paymentLog;

    @Embedded // payment 주문결제상세 내용
    private PaymentOrderInfo paymentInfo;

    public LocalDateTime requestedAt; //결제 요청 시간

    private LocalDateTime approvedAt; // 결제 승인 시간

    @Builder
    public Payment(UUID orderId, String orderName, Long amount){
        this.id = PaymentId.of();
        this.status = PaymentStatus.READY;
        this.paymentInfo = new PaymentOrderInfo(orderId,orderName);
        this.requestedAt = LocalDateTime.now();
        this.amount = new PaymentAmount(amount);
    }

    //도메인 로직 만들기
    // 중복 결제 방지 도메인 로직
    public void verifyNotProcessed(){
        if(this.status != PaymentStatus.READY || this.status != PaymentStatus.IN_PROGRESS){
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }
    }




}
