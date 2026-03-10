package com.sparta.no1delivery.domain.ai.infrastructure;

import com.sparta.no1delivery.domain.ai.domain.AiLogRepository;
import com.sparta.no1delivery.domain.ai.domain.AiLogType;
import com.sparta.no1delivery.domain.ai.domain.service.AiClient;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenAiClient implements AiClient {

    private final ChatClient chatClient;
    private final UserDetails userDetails;

    public OpenAiClient(ChatClient.Builder builder, AiLogRepository aiLogRepository, UserDetails userDetails) {
        this.chatClient = builder
                .defaultAdvisors(new AiLogAdvisor(aiLogRepository)).build();
        this.userDetails = userDetails;
    }

    @Override
    public<T> List<T> generate(String system, String userPrompt, AiLogType type, Long userId, Class<T> clazz) {
        try{
            return chatClient.prompt()
                    .system(system)
                    .user(userPrompt)
                    .advisors(spec -> spec
                            .param("LOG_TYPE", type)
                            .param("USER_ID", userDetails.getId()))
                    .call()
                    .entity(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_API_ERROR);
        }
    }
}
