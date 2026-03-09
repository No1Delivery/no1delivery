package com.sparta.no1delivery.domain.store.infrastructure;

import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.domain.store.infrastructure.query.StoreQueryRepositoryImpl;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.infrastructure.persistence.JPAConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Import({
        JPAConfig.class,
        StoreQueryRepositoryImpl.class,
        OwnerCheckImpl.class
})
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreQueryRepositoryImplTest {

    @Autowired
    private StoreQueryRepositoryImpl storeQueryRepository;

    @Autowired
    private EntityManager em;

    @MockitoBean
    private RoleCheck roleCheck;

    @MockitoBean
    private OwnerCheck ownerCheck;

    @Test
    @DisplayName("고객 - 가까운 가게 조회 테스트")
    void customerSearchTest() {
        // given
        // 가게 생성을 위한 owner 권한 부여
        given(roleCheck.hasRole("OWNER")).willReturn(true);
        // OwnerCheck가 항상 통과하도록 Mock 설정
        given(ownerCheck.isOwner(org.mockito.ArgumentMatchers.any())).willReturn(true);
        given(ownerCheck.getOwnerId()).willReturn(1L);

        createStore("가까운 가게", StoreStatus.OPEN, "가까운주소", "1층");  // 약 100m 거리
        createStore("먼 가게", StoreStatus.OPEN, "먼주소", "1층");         // 약 10km 이상
        createStore("폐업 가게", StoreStatus.DEFUNCT, "가까운주소", "2층"); // 가까운 폐업 가게

        // 조회를 위한 권한 변경
        given(roleCheck.hasRole("OWNER")).willReturn(false);
        given(roleCheck.hasRole("CUSTOMER")).willReturn(true);

        StoreQueryDto.Search search = StoreQueryDto.Search.builder()
                .latitude(37.0)
                .longitude(127.0)
                .radiusKm(3.0)
                .build();

        // when
        Page<Store> result = storeQueryRepository.findAll(search, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("가까운 가게");
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    private void createStore(String name, StoreStatus status, String address, String detailAddress) {
        Store store = Store.builder()
                .name(name)
                .ownerName("가짜 사장")
                .address(address)
                .detailAddress(detailAddress)
                .addressToCoords(testAddressToCoords)
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .build();

        store.changeStatus(roleCheck, ownerCheck, status);

        em.persist(store);
        em.flush();
        em.clear();
    }

    // 테스트용 좌표 변환기
    private final AddressToCoords testAddressToCoords = address -> {
        if (address.contains("강남역")) return new double[]{37.4979, 127.0276};
        if (address.contains("판교역")) return new double[]{37.4020, 127.1086};
        if (address.contains("가까운주소")) return new double[]{37.001, 127.001};
        if (address.contains("먼주소")) return new double[]{37.1, 127.1};
        return new double[]{0.0, 0.0};
    };
}
