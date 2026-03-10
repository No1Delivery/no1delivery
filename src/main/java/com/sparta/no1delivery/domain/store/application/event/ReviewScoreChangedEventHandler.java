package com.sparta.no1delivery.domain.store.application.event;

import com.sparta.no1delivery.domain.review.domain.event.ReviewScoreChangedEvent;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

// 리뷰 평균 평점 업데이트 처리 이벤트 핸들러
@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewScoreChangedEventHandler {

    private final StoreRepository storeRepository;

    @Async
    @Retryable(
            retryFor = { Exception.class },
            noRetryFor = { CustomException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000, multiplier = 2.0)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ReviewScoreChangedEvent event) {
        Store store = getStore(event.storeId());
        store.systemUpdateRating(event.averageScore(), event.reviewCount());
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    @Recover
    public void completeFailure(Exception e, ReviewScoreChangedEvent event) {
        log.error("가게별 리뷰 평균 평점 변경 최종 실패. 사유: {}. 가게 ID: {}, 평균 평점: {}, 리뷰 수: {}",
                e.getMessage(), event.storeId(), event.averageScore(), event.reviewCount(), e);
    }
}
