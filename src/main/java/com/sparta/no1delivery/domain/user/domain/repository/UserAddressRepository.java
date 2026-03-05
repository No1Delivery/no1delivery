package com.sparta.no1delivery.domain.user.domain.repository;

import com.sparta.no1delivery.domain.user.domain.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    List<UserAddress> findByUserUserId(UUID userId);

}