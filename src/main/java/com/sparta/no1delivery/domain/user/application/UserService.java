package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.user.domain.*;
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
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
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
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .detailAddress(detailAddress)
                .isDefault(isDefault)
                .build();

        user.addAddress(userAddress);
    }
}