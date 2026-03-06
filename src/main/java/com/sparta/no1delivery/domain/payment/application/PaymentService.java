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
        Payment payment = paymentRepository.findByPaymentInfoOrderId(UUID.fromString(orderId))
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        try {
            PaymentApproveResponse response = paymentClient.requestApprove(paymentKey, orderId, amount);

            payment.approve(
                    response.paymentKey(),
                    response.approvedAt(),
                    response.paymentLog(),
                    response.approvedAmount()
            );
        } catch (Exception ex) {
            payment.abort("결제 승인 중 에러 발생 : "+ ex.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_CONFIRM_FAILED);
        }
    }

    @Transactional
    public void cancelPayment( String orderId, String reason) {
        Payment payment = paymentRepository.findByPaymentInfoOrderId(UUID.fromString(orderId))
                .orElseThrow(()-> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        try {
            PaymentApproveResponse response = paymentClient.requestCancel(payment.getKey(), reason);

            payment.cancel(response.paymentLog(), response.approvedAt());
        } catch (Exception ex) {
            payment.failCancel("결제 취소 중 에러 발생 : " +ex.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }
    }
}