package com.sparta.no1delivery.domain.ai.infrastructure;

import com.sparta.no1delivery.domain.ai.domain.AiLog;
import com.sparta.no1delivery.domain.ai.domain.AiLogRepository;
import com.sparta.no1delivery.domain.ai.domain.AiLogType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

@RequiredArgsConstructor
public class AiLogAdvisor implements CallAdvisor {

    private final AiLogRepository aiLogRepository;

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        AiLogType type = (AiLogType) request.context().get("LOG_TYPE");
        Long userId = (Long) request.context().get("USER_ID");

        // [Before] 요청 텍스트 추출
        String prompt = request.prompt().getUserMessage().getText();

        // AI 호출
        ChatClientResponse response = chain.nextCall(request);

        // [After] AI의 응답 텍스트 추출
        String aiResponse = "No Response";

        if (response.chatResponse() != null) {
            aiResponse = response.chatResponse().getResult().getOutput().getText();
        }

        // DB 저장
        AiLog log = AiLog.builder()
                .prompt(prompt)
                .response(aiResponse)
                .type(type)
                .userId(userId)
                .build();

        aiLogRepository.save(log);

        return response;
    }

}