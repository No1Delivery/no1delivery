package com.sparta.no1delivery.domain.order.application;

//가게 조회 , 메뉴 검증, Order 생성 (상태: CREATED)
// 결제 요청 (PaymentService 호출), 결제 성공 시 → order.markPaid() , 저장

import com.sparta.no1delivery.domain.order.domain.*;
import com.sparta.no1delivery.domain.order.presentation.OrderRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성 메서드
    public UUID createOrder(OrderRequestDto request, Long userId) {

        // OrderItem 변환 (옵션은 일단 빈 리스트)
        List<OrderItem> items = request.getItems().stream()
                .map(item -> new OrderItem(
                        item.getMenuId(),
                        item.getMenuName(),
                        item.getMenuOption(),
                        item.getQuantity(),
                        item.getMenuPrice()
                ))
                .toList();

        //Store 정보 엔티티 생성
        StoreInfo storeInfo = new StoreInfo(
                request.getStoreId(),
                request.getStoreName()
        );

        //Delivery 정보 엔티티 생성
        DeliveryInfo deliveryInfo = new DeliveryInfo(
                request.getDeliveryAddress(),
                request.getDeliveryAddressDetail(),
                request.getDeliveryMemo(),
                request.getPhone()
        );

        //Order 엔티티 생성
        Order order = Order.createOrder(
                userId,
                request.getOrdererName(),
                storeInfo,
                deliveryInfo,
                items
        );

        // TODO: 메뉴/스토어 검증 로직 추가 (나중에 구현)
        // TODO: 결제 요청 호출 및 결제 완료 시 order.markPaid() 추가
        // TODO: 주문 생성 이벤트 발행 (OrderCreatedEvent) 추가


        //DB에 주문 저장
        orderRepository.save(order);

        return order.getOrderId();
    }
}