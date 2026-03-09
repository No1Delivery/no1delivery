package com.sparta.no1delivery.global.infrastructure.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoAddressToCoordsTest {

    @Autowired
    private KakaoAddressToCoords kakaoAddressToCoords;

    @Test
    @DisplayName("실제 주소를 넣었을 때 위도와 경도가 반환")
    void convertAddressToCoordsSuccess() {
        // given
        String address = "서울특별시 강남구 테헤란로 427";

        // when
        double[] coords = kakaoAddressToCoords.convert(address);

        // then
        assertThat(coords).isNotNull();
        assertThat(coords).hasSize(2);

        double latitude = coords[0];
        double longitude = coords[1];

        System.out.println("위도(Lat): " + latitude);
        System.out.println("경도(Lon): " + longitude);

        // 강남구 근처의 좌표인지 검증 (대략적인 범위)
        assertThat(latitude).isBetween(37.0, 38.0);
        assertThat(longitude).isBetween(126.0, 128.0);
    }

    @Test
    @DisplayName("존재하지 않는 주소를 넣으면 INVALID_ADDRESS 예외가 발생")
    void convertInvalidAddressThrowsException() {
        // given
        String invalidAddress = "존재할수않는주소";

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(com.sparta.no1delivery.global.presentation.exception.CustomException.class, () -> {
            kakaoAddressToCoords.convert(invalidAddress);
        });
    }
}