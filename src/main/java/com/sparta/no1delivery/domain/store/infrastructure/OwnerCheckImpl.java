package com.sparta.no1delivery.domain.store.infrastructure;

import com.sparta.no1delivery.domain.store.domain.QStore;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OwnerCheckImpl implements OwnerCheck {

    private final StoreRepository storeRepository;


    @Override
    public boolean isOwner(UUID storeId) {
        // TODO: 추후 Security 연동 시 실제 로그인한 사용자와 가게 주인 ID 비교 로직 추가
        // 지금은 테스트를 위해 무조건 true 반환
        return true;
    }

    @Override
    public Long getOwnerId() {
        // 테스트용 가게 주인 아이디로 1L 고정 반환
        return 1L;
    }

    @Override
    public String getOwnerName() {
        // 테스트용 이름
        return "임시 사장님";
    }

    @Override
    public UUID getStoreId() {
        // 삭제
        return null;
    }
}
