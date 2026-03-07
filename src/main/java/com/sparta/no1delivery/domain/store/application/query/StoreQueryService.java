package com.sparta.no1delivery.domain.store.application.query;

import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.StoreQueryRepository;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreResponseDto;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreQueryRepository storeQueryRepository;

    public StoreResponseDto getStore(UUID storeId) {
        return storeQueryRepository.findById(StoreId.of(storeId))
                .map(StoreResponseDto::fromDetail)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    public Page<StoreResponseDto> searchStores(StoreQueryDto.Search search, Pageable pageable) {
        Pageable validatedPageable = getValidatedPageable(pageable);

        return storeQueryRepository.findAll(search, validatedPageable)
                .map(StoreResponseDto::fromList);
    }

    private Pageable getValidatedPageable(Pageable pageable) {
        int pageSize = pageable.getPageSize();

        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            pageSize = 10;
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageSize,
                pageable.getSort()
        );
    }
}
