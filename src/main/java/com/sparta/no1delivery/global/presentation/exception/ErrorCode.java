package com.sparta.no1delivery.global.presentation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // --- 공통 에러 (C) ---
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // --- 인증 및 유저 (A, U) ---
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 정보가 일치하지 않습니다."),

    // --- 권한 신청 (OR) ---
    DUPLICATE_OWNER_REQUEST(HttpStatus.BAD_REQUEST, "이미 처리 중인 신청이 존재합니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청 내역입니다."),
    ALREADY_PROCESSED_REQUEST(HttpStatus.BAD_REQUEST, "이미 승인되거나 거절된 요청입니다."),
    OWNER_NOT_APPROVED(HttpStatus.BAD_REQUEST, "승인되지 않은 OWNER 권한입니다."),

    // --- 가게 (S) ---
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 가게의 관리 권한이 없습니다."),

    // --- 메뉴 (M) ---
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    INVALID_MENU_PRICE(HttpStatus.BAD_REQUEST, "메뉴 가격은 0원 이상이어야 합니다."),
    INVALID_OPTION_DATA(HttpStatus.BAD_REQUEST, "옵션 그룹 데이터가 유효하지 않습니다."),
    DUPLICATE_MENU_NAME(HttpStatus.CONFLICT, "동일한 이름의 메뉴가 이미 존재합니다."),

    // --- 옵션 (O) ---
    ESSENTIAL_OPTION_MISSING(HttpStatus.BAD_REQUEST, "필수 옵션을 선택해야 합니다."),
    MULTIPLE_OPTION_FORBIDDEN(HttpStatus.BAD_REQUEST, "옵션을 중복 선택할 수 없습니다."),
    OPTION_SOLD_OUT(HttpStatus.BAD_REQUEST, "품절된 옵션 항목이 포함되어 있습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}