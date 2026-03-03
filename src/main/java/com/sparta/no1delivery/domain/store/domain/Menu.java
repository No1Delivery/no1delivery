package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.Price;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@ToString
@Entity
@Table(name = "P_MENU", indexes = {
        @Index(name = "idx_menu_code", columnList = "menuCode")
})
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseUserEntity {

    @EmbeddedId
    private MenuId id;

    @Column(length = 45, unique = true, nullable = false)
    private String menuCode; // 메뉴 관리 코드

    private String name;

    private String description;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private MenuStatus status; // 메뉴 판매 상태

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "value", column = @Column(name = "price"))
    )
    private Price price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_MENU_OPTION", joinColumns = {
            @JoinColumn(name = "store_id"),
            @JoinColumn(name = "menu_idx")
    })
    @OrderColumn(name = "option_idx")
    private List<MenuOption> options;

}
