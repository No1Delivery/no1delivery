package com.sparta.no1delivery.domain.payment.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.no1delivery.domain.payment.domain.Payment;
import com.sparta.no1delivery.domain.payment.domain.PaymentId;
import com.sparta.no1delivery.domain.payment.domain.PaymentRepository;
import com.sparta.no1delivery.domain.payment.domain.PaymentStatus;
import com.sparta.no1delivery.domain.payment.domain.service.PaymentClient;
import com.sparta.no1delivery.domain.payment.domain.service.PaymentClientDto;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentClient implements PaymentClient {
    private final TossPaymentHelper helper;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentClientDto requestApprove(String paymentKey, UUID orderId, Long amount) {
        String idempotencyKey = orderId.toString() + "-order";

        log.info("요청 데이터: {}, {}, {}, {}", paymentKey, orderId, amount, idempotencyKey);
        PaymentClientDto.PaymentClientDtoBuilder builder = PaymentClientDto.builder()
                .key(paymentKey);

        try {
            JsonNode result = helper.getClient()
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/confirm")
                            .build()
                    )
                    .header("Idempotency-Key", idempotencyKey)
                    .body(Map.of("paymentKey", paymentKey, "orderId", orderId, "amount", amount))
                    .retrieve()
                    .body(JsonNode.class);

            // 토스 결제 승인 완료
            JsonNode statusNode = result.get("status");
            PaymentStatus status = PaymentStatus.valueOf(statusNode.asText());
            LocalDateTime approvedAt = result.get("approvedAt") == null ? null : LocalDateTime.parse(result.get("approvedAt").asText(), DateTimeFormatter.ISO_DATE_TIME);
            int approvedAmount = result.get("totalAmount") == null ? 0 : result.get("totalAmount").asInt(0);

            return builder.success(true)
                    .approvedAt(approvedAt)
                    .status(status)
                    .approvedAmount(approvedAmount)
                    .paymentLog(result == null ? null : result.toString())
                    .build();

        } catch (RestClientResponseException e) {
            // 응답 코드 4xx, 5xx
            log.error("여기 유입: {}, {}", e.getMessage(), e);
            JsonNode result = e.getResponseBodyAs(JsonNode.class);
            String code = result == null ? null : result.get("code").asText();
            String message = result == null ? null : result.get("message").asText();

            return builder.success(false)
                    .reason("[%s]%s".formatted(code, message))
                    .paymentLog(result == null ? null : result.toString())
                    .build();
        } catch (Exception e) { // 네트워크 타임아웃 또는 기타 예외
            return builder.success(false)
                    .reason("[%s]%s".formatted("UNKNOWN", e.getMessage()))
                    .build();

        }
    }

    @Override
    public PaymentClientDto requestCancel(PaymentId paymentId, String paymentKey, String reason) {

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        UUID orderId = payment.getPaymentInfo().getOrderId();
        String idempotencyKey = orderId.toString() + "-cancel ";

        PaymentClientDto.PaymentClientDtoBuilder builder = PaymentClientDto.builder()
                .key(paymentKey);

        try {
            JsonNode result = helper.getClient().post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{paymentKey}/cancel")
                            .build(paymentKey))
                    .header("Idempotency-Key", idempotencyKey)
                    .body(Map.of("cancelReason", reason))
                    .retrieve()
                    .body(JsonNode.class);

            return builder
                    .success(true)
                    .paymentLog(result == null ? null : result.toString())
                    .build();

        } catch (RestClientResponseException e) {
            JsonNode result = e.getResponseBodyAs(JsonNode.class);
            String code = result == null || result.get("code") == null ? "UNKNOWN":result.get("code").asText();
            String message = result == null || result.get("message") == null ? "UNKNOWN":result.get("message").asText();


            return builder
                    .success(false)
                    .reason("[%s]%s".formatted(code, message))
                    .paymentLog(result == null ? null : result.toString())
                    .build();
        } catch (Exception e) {
            // 네트워크 타임아웃 또는 기타 예외
            return  builder.success(false)
                    .reason("[%s]%s".formatted("UNKNOWN", e.getMessage()))
                    .build();
        }
    }
}
