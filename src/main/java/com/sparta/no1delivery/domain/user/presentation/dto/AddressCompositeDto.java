package com.sparta.no1delivery.domain.user.presentation.dto;

import com.sparta.no1delivery.domain.user.domain.entity.UserAddress;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressCompositeDto {

    // 배송지 등록 및 수정request
    public record SaveRequest(
            String address,
            String detailAddress,
            Boolean isDefault
    ){}

    // 배송지 response
    public record Response(
            UUID addressId,
            String address,
            String detailAddress,
            Boolean isDefault
    ){
        public static Response from(UserAddress userAddress) {
            return new Response(
              userAddress.getAddressIdx(),
              userAddress.getAddress(),
              userAddress.getDetailAddress(),
              userAddress.isDefault()
            );
        }
    }

}
