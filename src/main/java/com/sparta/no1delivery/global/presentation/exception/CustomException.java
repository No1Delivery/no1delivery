package com.sparta.no1delivery.global.presentation.exception;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.StandardCharsets;

@Getter
public class CustomException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(
                errorCode.getStatus(),
                errorCode.getMessage(),
                HttpHeaders.EMPTY,
                errorCode.getMessage().getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );
        this.errorCode = errorCode;
    }
}
