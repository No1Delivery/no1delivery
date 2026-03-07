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

    // --- 외부 API 연동 (E) ---
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "유효하지 않은 주소입니다. 주소를 다시 확인해주세요."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 서비스 연동 중 오류가 발생했습니다."),
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "외부 서비스 응답 시간이 초과되었습니다."),

    // --- 인증 및 유저 (A, U) ---
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 정보가 일치하지 않습니다."),
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 2자 이상 20자 이하여야 합니다."),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "비밀번호는 최소 8자 이상이어야 합니다."),
    PASSWORD_TOO_LONG(HttpStatus.BAD_REQUEST, "비밀번호는 최대 20자 이하여야 합니다."),

    // --- 배송지 (AD) ---
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 배송지를 찾을 수 없습니다."),
    DEFAULT_ADDRESS_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "기본 배송지는 삭제할 수 없습니다."),
    ADDRESS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "배송지는 최대 5개까지 등록할 수 있습니다."),

    // --- 권한 신청 (OR) ---
    DUPLICATE_OWNER_REQUEST(HttpStatus.BAD_REQUEST, "이미 처리 중인 신청이 존재합니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청 내역입니다."),
    ALREADY_PROCESSED_REQUEST(HttpStatus.BAD_REQUEST, "이미 승인되거나 거절된 요청입니다."),
    OWNER_NOT_APPROVED(HttpStatus.BAD_REQUEST, "승인되지 않은 OWNER 권한입니다."),

    // --- 가게 (S) ---
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 가게의 관리 권한이 없습니다."),
    INVALID_STORE_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 가게 상태값입니다."),

    // --- 메뉴 (M) ---
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    INVALID_MENU_PRICE(HttpStatus.BAD_REQUEST, "메뉴 가격은 0원 이상이어야 합니다."),
    INVALID_OPTION_DATA(HttpStatus.BAD_REQUEST, "옵션 그룹 데이터가 유효하지 않습니다."),
    DUPLICATE_MENU_NAME(HttpStatus.CONFLICT, "동일한 이름의 메뉴가 이미 존재합니다."),
    INVALID_MENU_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 메뉴 상태값입니다."),

    // --- 옵션 (O) ---
    DUPLICATE_OPTION_NAME(HttpStatus.CONFLICT, "중복된 옵션 이름이 존재합니다."),
    DUPLICATE_SUB_OPTION_NAME(HttpStatus.CONFLICT, "중복된 서브 옵션 이름이 존재합니다."),
    ESSENTIAL_OPTION_MISSING(HttpStatus.BAD_REQUEST, "필수 옵션을 선택해야 합니다."),
    MULTIPLE_OPTION_FORBIDDEN(HttpStatus.BAD_REQUEST, "옵션을 중복 선택할 수 없습니다."),
    OPTION_SOLD_OUT(HttpStatus.BAD_REQUEST, "품절된 옵션 항목이 포함되어 있습니다."),

    // --- 주문 (OD) ---
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    ORDER_ITEM_EMPTY(HttpStatus.BAD_REQUEST, "주문 항목은 최소 1개 이상이어야 합니다."),
    INVALID_ORDER_ITEM(HttpStatus.BAD_REQUEST, "주문이 불가능한 메뉴가 포함되어 있습니다."),
    ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    ORDER_CANCEL_TIME_EXPIRED(HttpStatus.BAD_REQUEST, "주문 접수 후 5분이 지나 취소할 수 없습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "현재 주문 상태에서는 수행할 수 없는 작업입니다."),

    // --- 카테고리 (CA)---
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    DUPLICATE_CATEGORY_NAME(HttpStatus.CONFLICT, "이미 존재하는 카테고리 이름입니다."),
    INVALID_DISPLAY_ORDER(HttpStatus.BAD_REQUEST, "유효하지 않은 노출 순서 값입니다."),
    CATEGORY_HAS_STORES(HttpStatus.BAD_REQUEST, "가게가 연결된 카테고리는 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}