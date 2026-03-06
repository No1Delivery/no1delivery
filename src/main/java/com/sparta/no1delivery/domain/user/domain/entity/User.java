package com.sparta.no1delivery.domain.user.domain.entity;

import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_user")
public class User extends BaseUserEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String loginId; // 로그인용 ID

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String nickname;

    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerRequestStatus ownerRequestStatus;

    private LocalDateTime roleUpdatedAt;

    private LocalDateTime ownerRequestAt;

    // User(1) : UserAddress(N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    @Builder
    private User(String loginId,
                 String password,
                 UserRole role,
                 String nickname,
                 String businessNumber,
                 OwnerRequestStatus ownerRequestStatus) {
        this.loginId = loginId;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.businessNumber = businessNumber;
        this.ownerRequestStatus = ownerRequestStatus;
    }

    //회원탈퇴 메소드 추가

    public void changePassword(String encodedPassword) {

        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        if(encodedPassword.length() < 8) {
            throw new CustomException(ErrorCode.PASSWORD_TOO_SHORT);
        }
        if(encodedPassword.length() > 20){
            throw new CustomException(ErrorCode.PASSWORD_TOO_LONG);
        }

        this.password = encodedPassword;
    }

    public void changeNickname(String nickname) {

        if (nickname == null || nickname.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        if (nickname.length() < 2 || nickname.length() > 20) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }

        if (this.nickname.equals(nickname)) {
            return;
        }

        this.nickname = nickname;
    }

    public void changeRole(UserRole role) {
        this.role = role;
        this.roleUpdatedAt = LocalDateTime.now();
    }

    //권한 요청
    public void requestOwnerRole(String businessNumber) {

        if (this.ownerRequestStatus == OwnerRequestStatus.PENDING) {
            throw new CustomException(ErrorCode.DUPLICATE_OWNER_REQUEST);
        }

        if (this.role == UserRole.OWNER) {
            throw new CustomException(ErrorCode.ALREADY_PROCESSED_REQUEST);
        }

        if (businessNumber == null || businessNumber.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        this.businessNumber = businessNumber;
        this.ownerRequestStatus = OwnerRequestStatus.PENDING;
        this.ownerRequestAt = LocalDateTime.now();
    }

    //권한 승인
    public void approveOwnerRole() {
        if (this.ownerRequestStatus != OwnerRequestStatus.PENDING) {
            throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
        }
        this.role = UserRole.OWNER;
        this.ownerRequestStatus = OwnerRequestStatus.NONE;
        this.roleUpdatedAt = LocalDateTime.now();
    }

    public void downgradeToCustomer() {

        if (this.role != UserRole.OWNER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        this.role = UserRole.CUSTOMER;
        this.roleUpdatedAt = LocalDateTime.now();

        // 상태 초기화
        this.ownerRequestStatus = OwnerRequestStatus.NONE;
    }

    public void rejectOwnerRole() {//권한 거절

        if (this.ownerRequestStatus != OwnerRequestStatus.PENDING) {
            throw new CustomException(ErrorCode.REQUEST_NOT_FOUND);
        }

        this.ownerRequestStatus = OwnerRequestStatus.REJECTED;
    }

    public void addAddress(UserAddress address) {

        if (this.addresses.size() >= 5) {
            throw new CustomException(ErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }

        // 첫 번째 주소라면 무조건 기본 배송지
        if (this.addresses.isEmpty()) {
            address.updateDefault(true);
        }
        // 첫 주소가 아니고, 새 주소가 기본으로 들어왔다면 기존 기본 해제
        else if (address.isDefault()) {
            this.addresses.forEach(a -> a.updateDefault(false));
        }

        address.setUser(this);
        this.addresses.add(address);
    }

    public void changeDefaultAddress(UserAddress newDefault) {

        if (!this.addresses.contains(newDefault)) {
            throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        this.addresses.forEach(a -> a.updateDefault(false));
        newDefault.updateDefault(true);
    }

    public void removeAddress(UserAddress address) {

        if (!this.addresses.contains(address)) {
            throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        if (address.isDefault()) {//이 주소가 기본 배송지라면
            throw new CustomException(ErrorCode.DEFAULT_ADDRESS_CANNOT_BE_DELETED);
        }

        this.addresses.remove(address);
    }

}
