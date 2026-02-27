package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@ToString
@Getter
@Table(name = "P_STORE")
@Access(AccessType.FIELD)
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

}
