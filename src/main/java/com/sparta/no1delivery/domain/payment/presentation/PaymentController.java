package com.sparta.no1delivery.domain.payment.presentation;

import com.sparta.no1delivery.domain.payment.application.PaymentService;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentCancelRequest;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentConfirmRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/payments")
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

    @GetMapping("/success")
    public void success(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount, HttpServletResponse response) throws IOException {
        paymentService.approvePayment(paymentKey,orderId,amount);
        response.sendRedirect("/demo/success.html");
    }

    @GetMapping("/fail")
    public void fail(@RequestParam String code,@RequestParam String message, @RequestParam String orderId, HttpServletResponse response)  throws IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect("/demo/fail.html?message=" + encodedMessage);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPayment(@RequestBody PaymentCancelRequest request){
        paymentService.cancelPayment(
                request.orderId(),
                request.reason()
        );
        return ResponseEntity.ok().build();
    }
}

