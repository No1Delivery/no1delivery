package com.sparta.no1delivery.domain.user.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_user")

public class User extends BaseUserEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String loginId; // 로그인용 ID

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String nickname;

    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerRequestStatus ownerRequestStatus;

    private LocalDateTime roleUpdatedAt;

    private LocalDateTime ownerRequestAt;

    // User(1) : UserAddress(N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserAddress> addresses = new ArrayList<>();

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeRole(UserRole role) {
        this.role = role;
        this.roleUpdatedAt = LocalDateTime.now();
    }

    public void addAddress(UserAddress address) {
        addresses.add(address);
        address.setUser(this);
    }
}
