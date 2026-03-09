package com.sparta.no1delivery.global.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoAddressToCoords implements AddressToCoords {

    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    private final RestClient restClient;

    public KakaoAddressToCoords(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://dapi.kakao.com").build();
    }

    @Override
    public double[] convert(String address) {
        if (!StringUtils.hasText(address)) return null;

        try {
            ResponseEntity<JsonNode> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .header("Authorization", "KakaoAK " + apiKey)
                    .retrieve()
                    .toEntity(JsonNode.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Kakao API 호출 실패 - 상태 코드: {}, 응답 바디: {}",
                        response.getStatusCode(),
                        response.getBody());
                throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
            }

            JsonNode body = response.getBody();

            if (body == null || !body.has("documents") || body.get("documents").isEmpty()) {
                throw new CustomException(ErrorCode.INVALID_ADDRESS);
            }

            JsonNode firstDoc = body.get("documents").get(0);

            double longitude = firstDoc.get("x").asDouble(); // 경도
            double latitude = firstDoc.get("y").asDouble(); // 위도

            log.info("Address: {} -> Coords: {}, {}", address, latitude, longitude);
            return new double[]{latitude, longitude};
        } catch (ResourceAccessException e) {
            log.error("Kakao API 연결 시간 초과 또는 네트워크 오류: {}", e.getMessage());
            throw new CustomException(ErrorCode.EXTERNAL_API_TIMEOUT);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Kakao API 호출 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

}
