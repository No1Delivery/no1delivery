package com.sparta.no1delivery.domain.store.application;

import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.service.CategoryCheck;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateStoreService {

    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final CategoryCheck categoryCheck;
    private final AddressToCoords addressToCoords;

    @Transactional
    public UUID create(StoreServiceDto.CreateStore dto) {
        String ownerName = StringUtils.hasText(dto.getOwnerName()) ? dto.getOwnerName() : ownerCheck.getOwnerName();

        Store store = Store.builder()
                .ownerName(ownerName)
                .name(dto.getName())
                .description(dto.getDescription())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .categoryIds(dto.getCategoryIds())
                .addressToCoords(addressToCoords)
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .build();

        storeRepository.save(store);

        return store.getId().getId();
    }
}
