package com.sparta.no1delivery.store.domain;

import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.dto.StoreDto;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class StoreMenuTest {

    private Store store;
    private RoleCheck roleCheck;
    private OwnerCheck ownerCheck;
    private UUID storeUuid;

    @BeforeEach
    void setUp() {
        storeUuid = UUID.randomUUID();
        roleCheck = Mockito.mock(RoleCheck.class);
        ownerCheck = Mockito.mock(OwnerCheck.class);

        // 기본 권한 설정 (OWNER)
        given(roleCheck.hasRole("OWNER")).willReturn(true);
        given(ownerCheck.isOwner(storeUuid)).willReturn(true);
        given(ownerCheck.getOwnerId()).willReturn(1L);

        // Store 생성
        store = Store.builder()
                .storeId(storeUuid)
                .ownerName("사장님")
                .name("맛있는 치킨")
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();
    }

    @Test
    @DisplayName("메뉴 생성 성공 및 중복 이름 차단 테스트")
    void createMenuTest() {
        // given
        StoreDto.MenuDto menuDto = StoreDto.MenuDto.builder()
                .name("후라이드 치킨")
                .description("바삭함")
                .price(18000)
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        // when
        store.createMenu(menuDto);

        // then
        assertThat(store.getMenus()).hasSize(1);
        assertThat(store.getMenus().get(0).getName()).isEqualTo("후라이드 치킨");

        // 중복 메뉴 생성 시도 시 예외 발생 검증
        assertThatThrownBy(() -> store.createMenu(menuDto))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.DUPLICATE_MENU_NAME.getMessage());
    }

    @Test
    @DisplayName("메뉴 수정 시 옵션이 통째로 교체되는지 확인")
    void updateMenuOptionsTest() {
        // given: 초기 메뉴 생성 (옵션 1개)
        StoreDto.MenuOptionDto option1 = new StoreDto.MenuOptionDto("무 추가", 500, null, false, false);
        StoreDto.MenuDto initialDto = StoreDto.MenuDto.builder()
                .name("치킨")
                .price(18000)
                .options(List.of(option1))
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();
        store.createMenu(initialDto);
        MenuId targetMenuId = store.getMenus().get(0).getId();

        // when: 새로운 옵션으로 수정 (옵션 2개)
        StoreDto.MenuOptionDto newOpt1 = new StoreDto.MenuOptionDto("콜라 사이즈업", 1000, null, false, false);
        StoreDto.MenuOptionDto newOpt2 = new StoreDto.MenuOptionDto("소스 추가", 500, null, false, false);
        StoreDto.MenuDto updateDto = StoreDto.MenuDto.builder()
                .name("치킨")
                .price(19000)
                .options(List.of(newOpt1, newOpt2))
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        store.updateMenu(targetMenuId, updateDto);

        // then
        Menu updatedMenu = store.getMenu(targetMenuId);
        assertThat(updatedMenu.getPrice().getValue()).isEqualTo(19000);
        assertThat(updatedMenu.getOptions()).hasSize(2);
        assertThat(updatedMenu.getOptions()).extracting("name")
                .containsExactly("콜라 사이즈업", "소스 추가");
    }

//    @Test
//    @DisplayName("메뉴 삭제 시 Soft Delete가 작동하는지 확인")
//    void removeMenuTest() {
//        // given
//        StoreDto.MenuDto menuDto = StoreDto.MenuDto.builder()
//                .name("삭제할 메뉴")
//                .roleCheck(roleCheck)
//                .ownerCheck(ownerCheck)
//                .build();
//        store.createMenu(menuDto);
//        MenuId targetMenuId = store.getMenus().get(0).getId();
//
//        // when
//        store.removeMenu(roleCheck, ownerCheck, targetMenuId);
//
//        // then: getMenu 호출 시 MENU_NOT_FOUND 예외 발생해야 함
//        assertThatThrownBy(() -> store.getMenu(targetMenuId))
//                .isInstanceOf(CustomException.class)
//                .hasMessageContaining(ErrorCode.MENU_NOT_FOUND.getMessage());
//    }
}