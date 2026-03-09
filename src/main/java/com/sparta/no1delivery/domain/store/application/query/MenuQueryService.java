package com.sparta.no1delivery.domain.store.application.query;

import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.MenuQueryRepository;
import com.sparta.no1delivery.domain.store.domain.query.dto.MenuQueryDto;
import com.sparta.no1delivery.domain.store.presentation.dto.MenuResponseDto;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuQueryService {

    public final MenuQueryRepository menuQueryRepository;

    public MenuResponseDto getMenu(UUID storeId, UUID menuId) {
        return menuQueryRepository.findById(StoreId.of(storeId), MenuId.of(menuId))
                .map(MenuResponseDto::fromDetail)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    public List<MenuResponseDto> getMenus(UUID storeId, MenuQueryDto.Search search) {
        return menuQueryRepository.findAll(StoreId.of(storeId), search)
                .stream()
                .map(MenuResponseDto::fromList)
                .toList();
    }
}
