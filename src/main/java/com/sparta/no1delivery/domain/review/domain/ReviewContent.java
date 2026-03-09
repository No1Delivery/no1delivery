package com.sparta.no1delivery.domain.review.domain;

import com.sparta.no1delivery.domain.review.domain.exception.ReviewRateOutOfRangeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.*;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewContent {

    @Column(nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String comment; // 리뷰 내용

    private int score; // 평점

    @Builder
    protected  ReviewContent(String subject, String comment, int score) {
        this.subject = subject;
        this.comment = comment;

        setScore(score);
    }

    private void setScore(int score) {
        // 리뷰의 평점은 필수이며 1~5점 사이 선택
        if (score < 1 || score > 5) {
            //throw new ReviewRateOutOfRangeException(score);
        }

        this.score = score;
    }
}
