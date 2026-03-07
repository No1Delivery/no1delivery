package com.sparta.no1delivery.domain.user.application.dto;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OwnerRequestDto {

    // Customer → Owner 권한 요청
    public record Request(
            String businessNumber
    ) {}

    // Manager → 요청 목록 조회 Response
    public record Response(
            BigInteger userId,
            String loginId,
            String nickname,
            String businessNumber,
            OwnerRequestStatus ownerRequestStatus,
            LocalDateTime ownerRequestAt
    ) {
        public static Response from(User user) {
            return new Response(
                    user.getUserId(),
                    user.getLoginId(),
                    user.getNickname(),
                    user.getBusinessNumber(),
                    user.getOwnerRequestStatus(),
                    user.getOwnerRequestAt()
            );
        }
    }
}