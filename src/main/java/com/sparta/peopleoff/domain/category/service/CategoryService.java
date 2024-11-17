package com.sparta.peopleoff.domain.category.service;

import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import java.util.UUID;

public interface CategoryService {

  void createCategory(CategoryRequestDto categoryRequestDto);

  void updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto);

}
