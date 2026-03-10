package com.sparta.no1delivery.domain.order.application.event;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderRepository;
import com.sparta.no1delivery.domain.order.domain.event.OrderPaymentConfirmedEvent;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentApprovedEvent;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentApprovedEventHandler {

    private final OrderRepository orderRepository;

    @Transactional
    @EventListener
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        UUID orderId = event.orderId();
        log.info("결제 완료 이벤트 수신 orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.paymentConfirm();
    }

    // 결제 성공 → 주문 조리 시작
    @Transactional
    @EventListener
    public void handle(OrderPaymentConfirmedEvent event) {

        UUID orderId = event.orderId();

        log.info("결제 승인 이벤트 수신 orderId={}", orderId);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 조리 시작 상태로 변경
        order.startPreparing();
    }
}