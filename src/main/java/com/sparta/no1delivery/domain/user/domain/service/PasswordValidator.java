package com.sparta.no1delivery.domain.user.domain.service;

public interface PasswordValidator {
    // orgPassword : 원 비밀번호, password: 사용자가 입력한 비밀번호
    boolean validate(String loginId, String password);
}
