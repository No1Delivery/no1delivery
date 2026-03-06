package com.sparta.no1delivery.domain.payment.infrastructure;

import com.sparta.no1delivery.domain.payment.domain.PaymentApproveResponse;
import com.sparta.no1delivery.domain.payment.domain.PaymentClient;
import com.sparta.no1delivery.domain.payment.infrastructure.dto.TossApproveResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class TossPaymentClient implements PaymentClient {

    private final TossPaymentProperties properties;
    private final RestTemplate restTemplate;

    public  TossPaymentClient(TossPaymentProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }


    @Override// payment키와 orderId, amount값을 함께 담아서 토스API에 전달하겠다는 규칙을 구현화 하는 것
    public PaymentApproveResponse requestApprove(String paymentKey, String orderId, Long amount) {
        String Url = properties.getBaseUrl() + "/confirm";

        Map<String, Object> request = new HashMap<>();
        request.put("paymentKey", paymentKey);
        request.put("orderId", orderId);
        request.put("amount", amount);

        return executeRequest(Url,request);
    }

    @Override
    public PaymentApproveResponse requestCancel(String paymentKey, String reason) {
        String Url = properties.getBaseUrl() + "/" + paymentKey + "/cancel";

        Map<String, Object> request = new HashMap<>();
        request.put("cancelReason", reason);

        return executeRequest(Url,request);
    }


    private PaymentApproveResponse executeRequest(String url, Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic "+ properties.getEncodeAuth());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TossApproveResponse> response = restTemplate.postForEntity(url,entity, TossApproveResponse.class);
        TossApproveResponse body = response.getBody();

        LocalDateTime at = LocalDateTime.parse(
                body.approvedAt(),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
        );
        return new PaymentApproveResponse(
                body.paymentKey(),
                body.totalAmount(),
                at,
                body.toString()
        );
    }
}
