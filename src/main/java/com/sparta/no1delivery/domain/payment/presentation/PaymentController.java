package com.sparta.no1delivery.domain.payment.presentation;

import com.sparta.no1delivery.domain.payment.application.PaymentService;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentCancelRequest;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentConfirmRequest;
import com.sparta.no1delivery.domain.payment.presentation.dto.PaymentFailRequest;
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
                request.orderId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/success")
    public void success(PaymentConfirmRequest request,  HttpServletResponse response) throws IOException {
        paymentService.approvePayment(request.paymentKey(),request.orderId());
        response.sendRedirect("/demo/success.html");
    }

    @GetMapping("/fail")
    public void fail(PaymentFailRequest request, HttpServletResponse response)  throws IOException {
        String encodedMessage = URLEncoder.encode("[%s]%s".formatted(request.code(), request.message()), StandardCharsets.UTF_8);
        response.sendRedirect("/demo/fail.html?message=" + encodedMessage);
    }

    @PostMapping("/cancel")
    public void cancelPayment(@RequestBody PaymentCancelRequest request){
        paymentService.cancelPayment(
                request.orderId(),
                request.reason()
        );
    }
}

