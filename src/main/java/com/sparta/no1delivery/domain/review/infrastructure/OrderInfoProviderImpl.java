package com.sparta.no1delivery.domain.review.infrastructure;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderItem;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryRepository;
import com.sparta.no1delivery.domain.review.domain.ReviewOrderInfo;
import com.sparta.no1delivery.domain.review.domain.ReviewOrderItem;
import com.sparta.no1delivery.domain.review.domain.service.OrderInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.sparta.no1delivery.domain.order.domain.OrderStatus.ORDER_DONE;

@Component
@RequiredArgsConstructor
public class OrderInfoProviderImpl implements OrderInfoProvider {

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public ReviewOrderInfo getOrderInfo(UUID orderId) {
        return orderQueryRepository.findById(orderId)
                .filter(this::isReviewable)
                .map(this::convertToReviewOrderInfo)
                .orElse(null); // Review 엔티티에서 null 체크 후 예외 발생
    }

    // 주문 상품이 없다면 정상적인 주문이 아니므로 리뷰 작성 불가
    // 주문 완료 상태 (DELIVERED)이 아니라면 리뷰 작성 불가
    private boolean isReviewable(Order order) {
        List<OrderItem> items = order.getOrderItems();
        return items != null && !items.isEmpty() && order.getStatus() == ORDER_DONE;
    }

    private ReviewOrderInfo convertToReviewOrderInfo(Order order) {
        List<ReviewOrderItem> reviewItems = order.getOrderItems().stream()
                .map(i -> new ReviewOrderItem(
                        i.getMenuName(),
                        i.getMenuPrice()
                ))
                .toList();

        return ReviewOrderInfo.builder()
                .orderId(order.getOrderId())
                .storeId(order.getStoreInfo().getStoreId())
                .storeName(order.getStoreInfo().getStoreName())
                .items(reviewItems)
                .build();
    }
}
