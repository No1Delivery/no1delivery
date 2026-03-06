package com.sparta.no1delivery.domain.user.presentation.dto;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCompositeDto {

    // 회원 정보 수정
    public record UpdateRequest(
            String nickname,
            String password
    ) {}

    // 회원정보 상제조회 응답
    public record DetailResponse(
            long userId,
            String loginId,
            String nickname,
            UserRole role,
            OwnerRequestStatus ownerRequestStatus,
            LocalDateTime roleUpdatedAt
    ) {
        public static DetailResponse from(User user) {
            return new DetailResponse(
                    user.getUserId(),
                    user.getLoginId(),
                    user.getNickname(),
                    user.getRole(),
                    user.getOwnerRequestStatus(),
                    user.getRoleUpdatedAt()
            );
        }
    }

    // 관리자용 회원 목록 조회 응답
    public record SummaryResponse(
            long userId,
            String loginId,
            String nickname,
            UserRole role
    ) {
        public static SummaryResponse from(User user) {
            return new SummaryResponse(
                    user.getUserId(),
                    user.getLoginId(),
                    user.getNickname(),
                    user.getRole()
            );
        }
    }

}
