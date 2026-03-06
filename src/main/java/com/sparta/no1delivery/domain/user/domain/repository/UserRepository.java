package com.sparta.no1delivery.domain.user.domain.repository;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

}