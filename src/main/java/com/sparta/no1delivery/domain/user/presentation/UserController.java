package com.sparta.no1delivery.domain.user.presentation;

import com.sparta.no1delivery.domain.user.application.UserService;
import com.sparta.no1delivery.domain.user.application.dto.OwnerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    //회원 정보 관련

    //배송지 관련

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
