package com.sparta.no1delivery.domain.user.domain.entity;

import com.sparta.no1delivery.domain.user.domain.enums.OwnerRequestStatus;
import com.sparta.no1delivery.domain.user.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private User createUser() {
        return User.builder()
                .loginId("testId")
                .password("1234")
                .nickname("두리")
                .role(UserRole.CUSTOMER)
                .ownerRequestStatus(OwnerRequestStatus.NONE)
                .build();
    }

    @Test
    void 유저_생성() {

        User user = createUser();

        assertThat(user.getLoginId()).isEqualTo("testId");
        assertThat(user.getNickname()).isEqualTo("두리");
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    void 닉네임_변경() {

        User user = createUser();

        user.changeNickname("두리짱");

        assertThat(user.getNickname()).isEqualTo("두리짱");
    }

    @Test
    void 비밀번호_변경() {

        User user = createUser();

        user.changePassword("encodedPassword");

        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void 주소_추가() {

        User user = createUser();

        UserAddress address = UserAddress.builder()
                .address("서울")
                .detailAddress("101호")
                .build();

        user.addAddress(address);

        assertThat(user.getAddresses().size()).isEqualTo(1);
    }

    @Test
    void 첫_주소는_기본주소() {

        User user = createUser();

        UserAddress address = UserAddress.builder()
                .address("서울")
                .detailAddress("101호")
                .build();

        user.addAddress(address);

        assertThat(address.isDefault()).isTrue();
    }

    @Test
    void 기본주소_삭제_불가() {

        User user = createUser();

        UserAddress address = UserAddress.builder()
                .address("서울")
                .detailAddress("101호")
                .build();

        user.addAddress(address);

        assertThatThrownBy(() -> user.removeAddress(address))
                .isInstanceOf(Exception.class);
    }

    @Test
    void 주소_삭제() {

        User user = createUser();

        UserAddress address1 = UserAddress.builder()
                .address("서울")
                .detailAddress("101호")
                .build();

        UserAddress address2 = UserAddress.builder()
                .address("부산")
                .detailAddress("202호")
                .build();

        user.addAddress(address1);
        user.addAddress(address2);

        user.removeAddress(address2);

        assertThat(user.getAddresses().size()).isEqualTo(1);
    }

    @Test
    void 사장님_권한_요청() {

        User user = createUser();

        user.requestOwnerRole("123-45-67890");

        assertThat(user.getOwnerRequestStatus())
                .isEqualTo(OwnerRequestStatus.PENDING);
    }

    @Test
    void 사장님_권한_승인() {

        User user = createUser();

        user.requestOwnerRole("123-45-67890");
        user.approveOwnerRole();

        assertThat(user.getRole()).isEqualTo(UserRole.OWNER);
    }

    @Test
    void 사장님_권한_거절() {

        User user = createUser();

        user.requestOwnerRole("123-45-67890");
        user.rejectOwnerRole();

        assertThat(user.getOwnerRequestStatus())
                .isEqualTo(OwnerRequestStatus.REJECTED);
    }

    @Test
    void 사장님_권한_다운그레이드() {

        User user = createUser();

        user.requestOwnerRole("123-45-67890");
        user.approveOwnerRole();

        user.downgradeToCustomer();

        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }
}