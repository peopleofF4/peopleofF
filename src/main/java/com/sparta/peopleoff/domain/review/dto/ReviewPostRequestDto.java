package com.sparta.peopleoff.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReviewPostRequestDto {

  @NotBlank
  private String comment;

  @NotNull
  @Min(1)
  @Max(5)
  private int rating;

  @NotNull
  private UUID orderId;
}