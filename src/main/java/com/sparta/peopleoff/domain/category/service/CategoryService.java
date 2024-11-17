package com.sparta.peopleoff.domain.category.service;

import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.dto.CategoryResponseDto;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

  void createCategory(CategoryRequestDto categoryRequestDto);

  void updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto);

  List<CategoryResponseDto> getCategory();

  void deleteCategory(UUID categoryId);
}
