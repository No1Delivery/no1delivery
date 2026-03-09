package com.sparta.no1delivery.domain.order.application.event;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderRepository;
import com.sparta.no1delivery.domain.payment.domain.event.PaymentFailedEvent;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedEventHandler {

    private final OrderRepository orderRepository;

    //결제 실패 → 주문 취소
    @Async
    @EventListener
    @Transactional
    public void handle(PaymentFailedEvent event) {

        UUID orderId = event.orderId();

        log.info("결제 실패 이벤트 수신 orderId={}", orderId);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 주문 취소 처리
        order.cancel();
    }
}