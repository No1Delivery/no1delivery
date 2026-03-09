package com.sparta.no1delivery.domain.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviewer {

    @Column(length = 45, name = "reviewer_id")
    private UUID id;

    @Column(length = 45)
    private String reviewerName;

}
