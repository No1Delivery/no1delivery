package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.entity.UserAddress;
import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressToCoords addressToCoords;
    // 회원가입
    public void signUp(String loginId,
                       String password,
                       String nickname) {

        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .role(UserRole.CUSTOMER)
                .nickname(nickname)
                .ownerRequestStatus(OwnerRequestStatus.NONE)
                .build();

        userRepository.save(user);
    }

    // user 조회
    @Transactional(readOnly = true)
    public User getUser(BigInteger userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    //닉네임 변경
    public void changeNickname(BigInteger userId, String nickname) {

        User user = getUser(userId);

        user.changeNickname(nickname);
    }

    //비밀번호 변경
    public void changePassword(BigInteger userId, String password) {

        User user = getUser(userId);
        String encodedPassword = passwordEncoder.encode(password);

        user.changePassword(encodedPassword);
    }

    //회원 탈퇴
    public void withdraw(BigInteger userId){
        User user = getUser(userId);

        //회원 탈퇴 메소드
    }

    // 주소 추가
    public void addAddress(
            BigInteger userId,
            String address,
            String detailAddress,
            Boolean isDefault
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        UserAddress userAddress = UserAddress.builder()
                .address(address)
                .detailAddress(detailAddress)
                .addressToCoords(addressToCoords)
                .isDefault(isDefault)
                .build();

        user.addAddress(userAddress);
    }

    //주소 수정
    public void updateAddress(
            BigInteger userId,
            UUID addressId,
            String address,
            String detailAddress
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        UserAddress userAddress = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        userAddress.updateAddress(address, detailAddress, addressToCoords);
    }


    //주소 삭제
    public void deleteAddress(
            BigInteger userId,
            UUID addressId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        UserAddress address = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.removeAddress(address);
    }

    //기본 배송지 변경
    public void changeDefaultAddress(
            BigInteger userId,
            UUID addressId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        UserAddress newDefault = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.changeDefaultAddress(newDefault);
    }

    //사장 권한 요청
    public void requestOwnerRole(BigInteger userId, String businessNumber) {

        User user = getUser(userId);

        user.requestOwnerRole(businessNumber);
    }

    //사장 권한 승인
    public void approveOwnerRole(BigInteger userId) {

        User user = getUser(userId);

        user.approveOwnerRole();
    }

    // 사장 권한 거절
    public void rejectOwnerRole(BigInteger userId) {

        User user = getUser(userId);

        user.rejectOwnerRole();
    }


    //사장 → 손님 권한 다운그레이드
    public void downgradeToCustomer(BigInteger userId) {

        User user = getUser(userId);

        user.downgradeToCustomer();
    }

    @Transactional(readOnly = true)
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}