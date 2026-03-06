package com.sparta.no1delivery.domain.payment.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossPaymentProperties {
    public static final String BASE_URL = "https://api.tosspayments.com/v1/payments";

    private final String encodeAuth;

    public TossPaymentProperties(@Value("${payment.toss.secret-key}")String secretKey){
        String auth = secretKey + ":";
        this.encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    public String getEncodeAuth(){
        return encodeAuth;
    }

    public String getBaseUrl(){
        return BASE_URL;
    }
}
