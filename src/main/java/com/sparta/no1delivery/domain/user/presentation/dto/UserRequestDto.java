package com.sparta.no1delivery.domain.user.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        @NotBlank(message = "아이디는 필수입력 값입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수입력 값입니다.")
        private String password;
    }

    @Getter
    //@NoArgsConstructor
    //@AllArgsConstructor
    public static class SignUp {

    }
}
