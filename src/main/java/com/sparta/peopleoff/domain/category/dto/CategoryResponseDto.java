package com.sparta.peopleoff.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryResponseDto {

  private String categoryName;

  public CategoryResponseDto(String categoryName) {
    this.categoryName = categoryName;
  }
}
