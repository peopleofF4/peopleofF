package com.sparta.peopleoff.domain.review.service;

import com.sparta.peopleoff.domain.review.dto.ReviewGetResponseDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.review.dto.ReviewPutRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  void registerReview(ReviewPostRequestDto requestDto, UserEntity user);

  List<ReviewGetResponseDto> getReviewsByStore(UUID storeId, Pageable pageable);

  List<ReviewGetResponseDto> getReviewsByUser(Long userId, Pageable pageable);

  ReviewGetResponseDto getReviewById(UUID reviewId);

  void updateReview(UUID reviewId, ReviewPutRequestDto requestDto, UserEntity user);

  void deleteReview(UUID reviewId, UserEntity user);

  List<ReviewGetResponseDto> searchReviews(String keyword, Pageable pageable);
}
