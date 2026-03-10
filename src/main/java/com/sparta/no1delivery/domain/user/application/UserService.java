package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.store.domain.Owner;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.user.application.dto.TokenDto;
import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.entity.UserAddress;
import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.domain.user.domain.service.PasswordValidator;
import com.sparta.no1delivery.domain.user.domain.service.TokenGenerator;
import com.sparta.no1delivery.domain.user.domain.vo.Token;
import com.sparta.no1delivery.domain.user.presentation.dto.AddressCompositeDto;
import com.sparta.no1delivery.domain.user.presentation.dto.OwnerRequestDto;
import com.sparta.no1delivery.domain.user.presentation.dto.UserCompositeDto;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.infrastructure.security.UserDetailsImpl;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final PasswordEncoder passwordEncoder;
    private final AddressToCoords addressToCoords;
    private final PasswordValidator passwordValidator;
    private final TokenGenerator tokenGenerator;

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

    //로그인
    public TokenDto.Token signIn(String loginId, String password) {
        User user = getUserByLoginId(loginId);
        Token token = user.signIn(password, passwordValidator, tokenGenerator);

        return TokenDto.Token
                .builder()
                .token(token.token())
                .refreshToken(token.refreshToken())
                .tokenExpireTime(token.tokenExpireTime())
                .refreshTokenExpireTime(token.refreshTokenExpireTime())
                .build();
    }

    // user 조회
    @Transactional(readOnly = true)
    public User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // user 개인 조회
    @Transactional(readOnly = true)
    public UserCompositeDto.DetailResponse getMyUser(Long userId){
        validateSelf(userId);
        User user = getUser(userId);
        return UserCompositeDto.DetailResponse.from(user);
    }

    //회원 목록 조회 (Manager)
    @Transactional(readOnly = true)
    public List<UserCompositeDto.SummaryResponse> getUsers() {

        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER")))
            throw new CustomException(ErrorCode.FORBIDDEN);

        return userRepository.findAll()
                .stream()
                .map(UserCompositeDto.SummaryResponse::from)
                .toList();
    }

    // 회원 정보 수정 (닉네임 + 비밀번호)
    public void updateUser(Long userId, String nickname, String password) {

        if (!roleCheck.hasRole("CUSTOMER"))
            throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        String encodedPassword = passwordEncoder.encode(password);

        user.updateUserInfo(nickname, encodedPassword);
    }
    //회원 탈퇴
    public void deleteUser(Long userId,UserDetails userDetails) {
        validateSelf(userId);
        User user = getUser(userId);
        user.deleteUser(userDetails);
    }

    //주소 조회
    @Transactional(readOnly = true)
    public List<AddressCompositeDto.Response> getAddresses(Long userId) {
        if (!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        return user.getAddresses()
                .stream()
                .map(AddressCompositeDto.Response::from)
                .toList();
    }

    // 주소 추가
    public void addAddress(Long userId,
                           String address,
                           String detailAddress,
                           Boolean isDefault) {
        if (!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        UserAddress userAddress = UserAddress.builder()
                .address(address)
                .detailAddress(detailAddress)
                .addressToCoords(addressToCoords)
                .isDefault(isDefault)
                .build();

        user.addAddress(userAddress);
    }

    // 주소 수정
    public void updateAddress(Long userId,
                              UUID addressId,
                              String address,
                              String detailAddress) {
        if (!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        UserAddress userAddress = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        userAddress.updateAddress(address, detailAddress, addressToCoords);
    }

    // 주소 삭제
    public void deleteAddress(Long userId, UUID addressId) {

        if (!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        UserAddress address = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.removeAddress(address);
    }

    // 기본 배송지 변경
    public void changeDefaultAddress(Long userId, UUID addressId) {

        if (!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        UserAddress newDefault = user.getAddresses()
                .stream()
                .filter(a -> a.getAddressIdx().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.changeDefaultAddress(newDefault);
    }


    //사장 권한 요청
    public void requestOwnerRole(Long userId, OwnerRequestDto.Request request) {

        if(!roleCheck.hasRole("CUSTOMER")) throw new CustomException(ErrorCode.FORBIDDEN);

        validateSelf(userId);

        User user = getUser(userId);

        user.requestOwnerRole(request.businessNumber());
    }

    //사장 신청 목록 조회
    @Transactional(readOnly = true)
    public List<OwnerRequestDto.Response> getOwnerRequests() {

        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER")))
            throw new CustomException(ErrorCode.FORBIDDEN);

        return userRepository.findAll()
                .stream()
                .filter(user -> user.getOwnerRequestStatus() == OwnerRequestStatus.PENDING)
                .map(OwnerRequestDto.Response::from)
                .collect(Collectors.toList());
    }

    //사장 권한 승인
    public void approveOwnerRole(Long userId) {

        if(!roleCheck.hasRole("MANAGER")) throw new CustomException(ErrorCode.FORBIDDEN);

        User user = getUser(userId);

        user.approveOwnerRole();
    }

    // 사장 권한 거절
    public void rejectOwnerRole(Long userId) {

        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER")))
            throw new CustomException(ErrorCode.FORBIDDEN);

        User user = getUser(userId);

        user.rejectOwnerRole();
    }

    //사장 → 손님 권한 다운그레이드
    public void downgradeToCustomer(Long userId) {

        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER")))
            throw new CustomException(ErrorCode.FORBIDDEN);

        User user = getUser(userId);

        user.downgradeToCustomer();
    }

    @Transactional(readOnly = true)
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Long getLoginUserId() {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            return userDetails.getUser().getUserId();
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    // 본인 검증 메서드
    private void validateSelf(Long userId) {

        Long loginUserId = getLoginUserId();

        if (!loginUserId.equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}