package com.sparta.no1delivery.domain.ai.domain.service;

import com.sparta.no1delivery.domain.ai.domain.AiLogType;

import java.util.List;

public interface AiClient {

    <T> List<T> generate(String system, String userPrompt, AiLogType type, Long userId, Class<T> clazz);
}
