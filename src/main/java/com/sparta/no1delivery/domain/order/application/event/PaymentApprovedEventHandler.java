package com.sparta.no1delivery.domain.order.application.event;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderRepository;
import com.sparta.no1delivery.domain.order.domain.event.OrderPaymentConfirmedEvent;
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
    public void handle(OrderPaymentConfirmedEvent event) {

        UUID orderId = event.orderId();

        log.info("결제 승인 이벤트 수신 orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.startPreparing();
    }
}