package com.sparta.no1delivery.global.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUserEntity extends BaseEntity {
    @CreatedBy
    @Column(updatable = false)
    protected Long createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    protected Long updatedBy;

    @Column
    protected Long deletedBy;

}