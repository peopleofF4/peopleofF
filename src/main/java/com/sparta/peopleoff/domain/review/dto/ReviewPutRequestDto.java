package com.sparta.peopleoff.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewPutRequestDto {

  @NotBlank
  private String comment;

  @NotNull
  @Min(1)
  @Max(5)
  private int rating;
}
