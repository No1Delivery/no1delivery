package com.sparta.no1delivery.domain.payment.infrastructure;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossPaymentHelper {

    private RestClient restClient;

    public TossPaymentHelper(@Value("${payment.toss.secret-key}")String secretKey) {
        String auth = secretKey + ":";
        String encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        restClient = RestClient.builder()
                .baseUrl(URI.create( "https://api.tosspayments.com/v1/payments"))
                .defaultHeaders((headers) -> {
                    headers.setBasicAuth(encodeAuth);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }


    public RestClient getClient() {
       return restClient;
    }
}
