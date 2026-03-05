package com.sparta.no1delivery.domain.payment.application;

import com.sparta.no1delivery.domain.payment.domain.Payment;
import com.sparta.no1delivery.domain.payment.domain.PaymentApproveResponse;
import com.sparta.no1delivery.domain.payment.domain.PaymentClient;
import com.sparta.no1delivery.domain.payment.domain.PaymentRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;

    @Transactional
    public void approvePayment(String paymentKey, String orderId, Long amount) {
        Payment payment =paymentRepository.findByOrderId(UUID.fromString(orderId))
                .orElseThrow(()-> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        PaymentApproveResponse response = paymentClient.requestApprove(paymentKey,orderId,amount);
    }
}