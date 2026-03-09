package com.sparta.no1delivery.domain.user.presentation;

import com.sparta.no1delivery.domain.user.application.UserService;
import com.sparta.no1delivery.domain.user.application.dto.TokenDto;
import com.sparta.no1delivery.domain.user.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //로그인
    @PostMapping("/auth/login")
    public UserResponseDto.Token signIn(@RequestBody @Valid UserRequestDto.SignIn request) {
        TokenDto.Token token = userService.signIn(request.getLoginId(), request.getPassword());

        return UserResponseDto.Token
                .builder()
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
                .tokenExpireTime(token.getTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();
    }

    //회원 정보 관련
    // 회원 정보 조회
    @GetMapping("/{userId}")
    public UserCompositeDto.DetailResponse getUser(@PathVariable Long userId) {

        return UserCompositeDto.DetailResponse.from(userService.getUser(userId));
    }

    // 회원 목록 조회 (관리자)
    @GetMapping
    public List<UserCompositeDto.SummaryResponse> getUsers() {

        return userService.getUsers();
    }

    // 회원 정보 수정 (닉네임, 비밀번호)
    @PatchMapping("/{userId}")
    public void updateUser(
            @PathVariable Long userId,
            @RequestBody UserCompositeDto.UpdateRequest request
    ) {

        userService.changeNickname(userId, request.nickname());
        userService.changePassword(userId, request.password());
    }

    //배송지 관련
// 배송지 등록
    @PostMapping("/{userId}/addresses")
    public void addAddress(
            @PathVariable Long userId,
            @RequestBody AddressCompositeDto.SaveRequest request
    ) {

        userService.addAddress(
                userId,
                request.address(),
                request.detailAddress(),
                request.isDefault()
        );
    }

    // 배송지 수정
    @PatchMapping("/{userId}/addresses/{addressId}")
    public void updateAddress(
            @PathVariable Long userId,
            @PathVariable UUID addressId,
            @RequestBody AddressCompositeDto.SaveRequest request
    ) {

        userService.updateAddress(
                userId,
                addressId,
                request.address(),
                request.detailAddress()
        );
    }

    // 배송지 삭제
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public void deleteAddress(
            @PathVariable Long userId,
            @PathVariable UUID addressId
    ) {

        userService.deleteAddress(userId, addressId);
    }

    // 배송지 목록 조회
    @GetMapping("/{userId}/addresses")
    public List<AddressCompositeDto.Response> getAddresses(
            @PathVariable Long userId
    ) {

        return userService.getAddresses(userId);
    }

    // 기본 배송지 변경
    @PatchMapping("/{userId}/addresses/{addressId}/default")
    public void changeDefaultAddress(
            @PathVariable Long userId,
            @PathVariable UUID addressId
    ) {

        userService.changeDefaultAddress(userId, addressId);
    }


    // 권한 관련

    // 사장 권한 요청
    @PostMapping("/{userId}/owner-request")
    public void requestOwnerRole(
            @PathVariable Long userId,
            @RequestBody OwnerRequestDto.Request request
    ) {

        userService.requestOwnerRole(userId, request);
    }

    // 사장 요청 목록 조회 (관리자)
    @GetMapping("/owner-requests")
    public List<OwnerRequestDto.Response> getOwnerRequests() {

        return userService.getOwnerRequests();
    }

    // 권한 승인
    @PostMapping("/{userId}/owner-approve")
    public void approveOwnerRole(@PathVariable Long userId) {

        userService.approveOwnerRole(userId);
    }

    // 권한 거절
    @PostMapping("/{userId}/owner-reject")
    public void rejectOwnerRole(@PathVariable Long userId) {

        userService.rejectOwnerRole(userId);
    }


}
