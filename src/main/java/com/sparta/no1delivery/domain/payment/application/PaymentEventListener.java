package com.sparta.no1delivery.domain.payment.application;

import com.sparta.no1delivery.domain.order.domain.event.OrderAcceptedEvent;
import com.sparta.no1delivery.domain.order.domain.event.OrderRefundedEvent;
import com.sparta.no1delivery.domain.payment.domain.Payment;
import com.sparta.no1delivery.domain.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @EventListener
    @Transactional  //OrderAcceptedEvent event 이거로 바뀌어야 함. 대신  String orderName, 추가 Long amount,추가 되야함
    public void handleOrderCreatedEvent(OrderAcceptedEvent event) {
        Payment payment = Payment.builder()
                .orderId(event.orderId())
                .orderName(event.orderName())
                .amount(event.amount())
                .build();

        paymentRepository.save(payment);
    }

    @EventListener
    @Transactional
    public void handleOrderCancelEvent(OrderRefundedEvent event) {
        paymentService.cancelPayment(
                event.orderId(),
                "주문 취소로 인한 자동 결제 취소"
        );
    }
}
