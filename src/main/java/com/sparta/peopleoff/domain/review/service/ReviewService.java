package com.sparta.peopleoff.domain.review.service;

import com.sparta.peopleoff.domain.review.dto.ReviewPostRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;

public interface ReviewService {

  void registerReview(ReviewPostRequestDto requestDto, UserEntity user);
}
