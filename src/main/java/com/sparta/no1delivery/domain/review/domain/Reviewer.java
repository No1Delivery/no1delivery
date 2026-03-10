package com.sparta.no1delivery.domain.review.domain;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reviewer {

    @Column(length = 45, name = "reviewer_id")
    private Long id;

    @Column(length = 45)
    private String reviewerName;

    protected Reviewer(UserDetails userDetails) {
        if (userDetails == null || userDetails.getId() == null) {
            throw new CustomException(ErrorCode.INVALID_REVIEWER_NOT_FOUND);
        }
    }

}
