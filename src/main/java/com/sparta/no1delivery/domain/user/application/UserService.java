package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.entity.UserAddress;
import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    // 회원가입
    public void signUp(String loginId,
                       String password,
                       String nickname) {

        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .loginId(loginId)
                .password(password)
                .role(UserRole.CUSTOMER)
                .nickname(nickname)
                .ownerRequestStatus(OwnerRequestStatus.NONE)
                .build();

        userRepository.save(user);
    }
    // 주소 추가
    public void addAddress(User user,
                           BigDecimal latitude,
                           BigDecimal longitude,
                           String address,
                           String detailAddress,
                           Boolean isDefault) {

        UserAddress userAddress = UserAddress.builder()
                //.latitude(latitude)
                //.longitude(longitude)
                .address(address)
                .detailAddress(detailAddress)
                .isDefault(isDefault)
                .build();

        user.addAddress(userAddress);
    }

    @Transactional(readOnly = true)
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}