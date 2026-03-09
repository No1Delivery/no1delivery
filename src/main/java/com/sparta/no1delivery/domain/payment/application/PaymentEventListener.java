package com.sparta.no1delivery.domain.payment.application;

import com.sparta.no1delivery.domain.order.event.OrderCreatedEvent;
import com.sparta.no1delivery.domain.payment.domain.Payment;
import com.sparta.no1delivery.domain.payment.domain.PaymentRepository;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentApprovedEvent;
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
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        Payment payment = Payment.builder()
                .orderId(event.orderId())
                .orderName(event.orderName())
                .amount(event.amount())
                .build();

        paymentRepository.save(payment);
    }

    @EventListener
    @Transactional
    public void handleOrderCancelEvent(PaymentApprovedEvent event) {
        paymentService.cancelPayment(
                event.orderId().toString(),
                "주문 취소로 인한 자동 결제 취소"
        );
    }
}
