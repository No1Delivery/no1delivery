package com.sparta.no1delivery.global.presentation.exception;

import org.springframework.http.HttpStatusCode;

public record ErrorResponse(
        HttpStatusCode status,
        String message
) {
}