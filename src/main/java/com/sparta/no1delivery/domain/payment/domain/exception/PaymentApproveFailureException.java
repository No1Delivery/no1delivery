package com.sparta.no1delivery.domain.payment.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class PaymentApproveFailureException extends HttpStatusCodeException {
    public PaymentApproveFailureException(String reason) {
        super(HttpStatus.BAD_REQUEST, "결제 승인 처리에 실패했습니다:" + reason);
    }
}
