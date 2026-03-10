package com.sparta.no1delivery.domain.order.application;

import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.domain.*;
import com.sparta.no1delivery.domain.order.domain.service.OptionCheck;
import com.sparta.no1delivery.domain.order.domain.service.OrderCheck;
import com.sparta.no1delivery.domain.order.domain.service.ProductProvider;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductProvider productProvider;
    private final OrderCheck orderCheck;
    private final OptionCheck optionCheck;

    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final UserDetails userDetails;


    // 배송지 변경
    public void changeDeliveryInfo(
            UUID orderId,
            String address,
            String detailAddress,
            String memo
    ) {

        Long userId = userDetails.getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getOrderer().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        order.changeDeliveryInfo(address, detailAddress, memo);
    }


    // 주문 생성
    public UUID createOrder(OrderServiceDto.Create dto) {

        Long userId = userDetails.getId();

        if (!roleCheck.hasRole("CUSTOMER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_ITEM_EMPTY);
        }

        Store store = productProvider.getStore(dto.getStoreId());

        List<OrderItem> items = dto.getItems().stream()
                .map(item -> {

                    Menu menu = productProvider.getMenu(dto.getStoreId(), item.getMenuId());

                    orderCheck.validateMenu(
                            menu,
                            dto.getStoreId(),
                            item.getMenuPrice()
                    );

                    if (item.getOptions() != null && !item.getOptions().isEmpty()) {

                        List<String> optionNames = item.getOptions().stream()
                                .map(OrderServiceDto.Option::getName)
                                .toList();

                        optionCheck.validate(menu, optionNames);
                    }

                    return toOrderItem(item);
                })
                .toList();


        StoreInfo storeInfo = new StoreInfo(
                dto.getStoreId(),
                dto.getStoreName()
        );

        DeliveryInfo deliveryInfo = new DeliveryInfo(
                dto.getDeliveryAddress(),
                dto.getDeliveryAddressDetail(),
                dto.getDeliveryMemo()
        );

        Order order = Order.createOrder(
                userId,
                dto.getOrdererName(),
                dto.getPhone(),
                storeInfo,
                deliveryInfo,
                items
        );

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getOrderId();
    }


    // DTO → OrderItem
    private OrderItem toOrderItem(OrderServiceDto.Item item) {

        if (item.getMenuPrice() <= 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }

        List<OrderServiceDto.Option> options =
                item.getOptions() == null ? Collections.emptyList() : item.getOptions();

        List<SelectedOption> selectedOptions = convertToSelectedOptions(options);

        return new OrderItem(
                item.getMenuId(),
                item.getMenuName(),
                selectedOptions,
                item.getQuantity(),
                item.getMenuPrice()
        );
    }


    // DTO Option → SelectedOption
    private List<SelectedOption> convertToSelectedOptions(List<OrderServiceDto.Option> options) {

        if (options == null || options.isEmpty()) {
            return Collections.emptyList();
        }

        return options.stream()
                .map(option -> {

                    List<SelectedOption.SelectedSubOption> subOptions =
                            option.getSubOptions() == null
                                    ? Collections.emptyList()
                                    : option.getSubOptions().stream()
                                    .map(sub -> SelectedOption.SelectedSubOption.builder()
                                            .name(sub.getName())
                                            .addPrice(sub.getPrice())
                                            .build())
                                    .toList();

                    return SelectedOption.builder()
                            .optionName(option.getName())
                            .optionPrice(0)   // 여기 수정됨
                            .subOptions(subOptions)
                            .build();
                })
                .toList();
    }


    // 주문 취소
    public void cancelOrder(UUID orderId) {

        Long userId = userDetails.getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!roleCheck.hasRole(List.of("MASTER", "MANAGER"))
                && !order.getOrderer().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        order.cancel();
    }


    // 주문 상태 변경
    public void changeOrderStatus(UUID orderId, OrderStatus status) {

        if (!roleCheck.hasRole("OWNER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!ownerCheck.isOwner(order.getStoreInfo().getStoreId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        switch (status) {
            case ORDER_ACCEPT -> order.orderAccept();
            case PREPARING -> order.startPreparing();
            case READY -> order.ready();
            case DELIVERY -> order.startDelivery();
            case DELIVERY_DONE -> order.deliveryDone();
            case ORDER_DONE -> order.complete();
            default -> throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }
}