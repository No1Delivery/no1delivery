package com.sparta.no1delivery.domain.payment.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.sparta.no1delivery.domain.payment.domain.PaymentClient;
import com.sparta.no1delivery.domain.payment.domain.PaymentApproveResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TossPaymentClient implements PaymentClient {

    private final String secretKey; // 내 시크릿키를 받아오는 필드

    public TossPaymentClient(@Value("${payment.toss.secret-key}")String secretKey) {
        this.secretKey = secretKey; //생성자 주입방식으로 yml파일을 통해 내 시크릿키를 안전하게 가져오기
    }

    @Override// payment키와 orderId, amount값을 함께 담아서 토스API에 전달하겠다는 규칙을 구현화 하는 것
    public PaymentApproveResponse requestApprove(String paymentKey, String orderId, Long amount) {
        //토스 API URL 필드를 만들어 넣기 => 이 후 취소나 실패에 동일하게 필요한 것 묶어서 코드화 시키면 좋을듯?
        String Url = "https://api.tosspayments.com/v1/payments/confirm";
        //인증 헤더 만들기 내 시크릿키와 토스에서 원하는 시크릿키방식을 맞춰서 auth에 저장
        String auth = secretKey + ":";
        // auth에 저장된 내 시크릿키를 안전하게 base64로 인코딩하여 만든 값
        String encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        // 토스 API에 전달할 정보를 하나씩이 아니라 한꺼번에 묶어서 전달하기 위함.
        Map<String, Object> request = new HashMap<>();
        // "paymentKey" 값으로 토스에서
        request.put("paymentKey", paymentKey);
        request.put("orderId", orderId);
        request.put("amount", amount);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic "+ encodeAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        //전화 걸기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(Url, entity, Map.class);

        //답변 정리
        Map<String, Object> body = response.getBody();
        String pKey = (String) body.get("paymentKey");
        String approvedAtStr = (String) body.get("appreovedAt");
        Long pAmount = Long.parseLong(String .valueOf(body.get("totalAmount")));
        LocalDateTime approvedAt = LocalDateTime.parse(approvedAtStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return new PaymentApproveResponse(
                pKey,
                pAmount,
                approvedAt,
                body.toString()
                );
    }
}
