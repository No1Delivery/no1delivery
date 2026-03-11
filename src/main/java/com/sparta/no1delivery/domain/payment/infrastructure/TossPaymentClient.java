package com.sparta.no1delivery.domain.payment.infrastructure;

import com.sparta.no1delivery.domain.payment.domain.PaymentClient;
import com.sparta.no1delivery.domain.payment.infrastructure.dto.TossApproveResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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


    @Override// payment 키와 orderId, amount값을 함께 담아서 토스API에 전달하겠다는 규칙을 구현화 하는 것
    public TossApproveResponse requestApprove(String paymentKey, String orderId, Long amount, String idempotencyKey) {
        String Url = properties.getBaseUrl() + "/confirm";

        Map<String, Object> request = new HashMap<>();
        request.put("paymentKey", paymentKey);
        request.put("orderId", orderId);
        request.put("amount", amount);

        return executeRequest(Url,request,idempotencyKey);
    }

    @Override
    public TossApproveResponse requestCancel(String paymentKey, String reason, String idempotencyKey) {
        String Url = properties.getBaseUrl() + "/" + paymentKey + "/cancel";

        Map<String, Object> request = new HashMap<>();
        request.put("cancelReason", reason);

        return executeRequest(Url,request,idempotencyKey);
    }


    private TossApproveResponse executeRequest(String url, Map<String, Object> requestBody, String idempotencyKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic "+ properties.getEncodeAuth());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Idempotency-Key", idempotencyKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TossApproveResponse> response = restTemplate.postForEntity(url,entity, TossApproveResponse.class);

        return response.getBody();
    }
}
