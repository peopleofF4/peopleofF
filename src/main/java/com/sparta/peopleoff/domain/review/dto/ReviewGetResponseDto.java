package com.sparta.peopleoff.domain.review.dto;

import com.sparta.peopleoff.domain.review.entity.ReviewEntity;
import lombok.Getter;

@Getter
public class ReviewGetResponseDto {
  private final String comment;
  private final int rating;
  private final String storeName;

  public ReviewGetResponseDto(ReviewEntity review) {
    this.comment = review.getComment();
    this.rating = review.getRating();
    this.storeName = review.getStore().getStoreName();
  }
}
