package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@ToString
@Getter
@Table(name = "P_STORE")
@Access(AccessType.FIELD)
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseUserEntity {

    @EmbeddedId
    private StoreId id;

    @Embedded
    private Owner owner;

    @Column(length = 65, name = "store_name")
    private String name;

    @Column(length = 100, name = "description")
    private String description;

    @Column(name = "phone")
    private String phone;

    @Embedded
    private StoreAddress address;

    @Embedded
    private Rating rating = new Rating(); // 초기값 지정

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_STORE_CATEGORY", joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "category_idx")
    private List<StoreCategory> categories;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "store_id")
    private List<Menu> menus;

}
