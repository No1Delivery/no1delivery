package com.sparta.no1delivery.global.domain.service;

// 인증한 사용자의 정보 조회
public interface UserDetails {
    Long getId();
    String getName();
    String getPhone();
    boolean isAuthenticated(); // 로그인 여부
}