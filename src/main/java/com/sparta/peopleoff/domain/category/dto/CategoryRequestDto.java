package com.sparta.peopleoff.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequestDto {

    @NotBlank(message = "카테고리명을 입력해주세요.")
    @Size(min = 1, max = 100, message = "카테고리명은 최대 100글자입니다.")
    private String categoryName;
}
