package com.sparta.no1delivery.domain.review.presentation;

import com.sparta.no1delivery.domain.review.application.ReviewService;
import com.sparta.no1delivery.domain.review.application.query.ReviewQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review API", description = "V1 리뷰 관리 API (등록/수정/삭제/조회)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewController {

   private final ReviewService reviewService;
   private final ReviewQueryService

}
