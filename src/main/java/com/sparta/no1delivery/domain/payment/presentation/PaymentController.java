package com.sparta.no1delivery.domain.payment.presentation;

import com.sparta.no1delivery.domain.payment.application.PaymentService;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentConfirmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentConfirmRequest request) {
        paymentService.approvePayment(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );

        return ResponseEntity.ok().build();

    }


}

