package com.sparta.no1delivery.domain.ai.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.no1delivery.domain.ai.domain.AiLogType;
import com.sparta.no1delivery.domain.ai.domain.service.AiClient;
import com.sparta.no1delivery.domain.ai.presentation.dto.MenuNamingResponse;
import com.sparta.no1delivery.domain.ai.presentation.dto.StoreDescriptionResponse;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;
    private final UserDetails userDetails;

    public List<MenuNamingResponse> generateMenuNamings(String features) {
        String system = """
            당신은 메뉴 네이밍 및 카피라이팅 전문가입니다.
            사용자가 제공한 특징을 바탕으로 메뉴 이름과 설명 후보 3개를 작성하세요.
            반드시 아래 JSON 형식으로만 응답하세요. 다른 부연 설명은 하지 마세요.

            형식:
            [
              {"name": "추천 이름 1", "description": "추천 설명 1"},
              {"name": "추천 이름 2", "description": "추천 설명 2"},
              {"name": "추천 이름 3", "description": "추천 설명 3"}
            ]
            """;

        return aiClient.generate(
                system,
                "메뉴 특징: " + features,
                AiLogType.MENU_NAMING,
                userDetails.getId(),
                MenuNamingResponse.class
        );
    }

    public List<StoreDescriptionResponse> generateStoreDescriptions(String storeFeatures) {
        String system = """
                당신은 감성적인 카피라이터이자 외식업 브랜딩 전문가입니다.
                사용자가 제공하는 가게의 특징을 바탕으로 고객의 마음을 사로잡는 '가게 소개글'을 작성하세요.
                내용은 친절하고 전문적이어야 하며, 150자 내외로 작성해 주세요.
                반드시 아래 JSON 형식으로만 응답하세요. 다른 부연 설명은 하지 마세요.
                
                [
                  { "description": "소개글 내용 1" },
                  { "description": "소개글 내용 2" },
                  { "description": "소개글 내용 3" }
                ]
                """;
        return aiClient.generate(
                system,
                "가게 특징: " + storeFeatures,
                AiLogType.STORE_DESCRIPTION,
                userDetails.getId(),
                StoreDescriptionResponse.class
        );
    }

}
