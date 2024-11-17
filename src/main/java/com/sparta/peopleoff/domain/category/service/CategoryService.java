package com.sparta.peopleoff.domain.category.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.dto.CategoryResponseDto;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * 카테고리 생성
   *
   * @param categoryRequestDto
   */
  @Transactional
  public void createCategory(CategoryRequestDto categoryRequestDto) {
    CategoryEntity category = new CategoryEntity(categoryRequestDto);
    categoryRepository.save(category);
  }

  /**
   * 카테고리 수정
   *
   * @param categoryId
   * @param categoryRequestDto
   */
  @Transactional
  public void updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto) {

    // [예외2] - 존재하지 않는 categoryId
    CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() ->
        new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 카테고리입니다."));

    category.setCategoryName(categoryRequestDto.getCategoryName());
  }

  /**
   * 카테고리 조회
   *
   * @return
   */
  @Transactional(readOnly = true)
  public List<CategoryResponseDto> getCategory() {

    List<CategoryEntity> categorys = categoryRepository.findAll();

    // [예외1] - 카테고리 없음
    if (categorys.isEmpty()) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "카테고리를 찾을 수 없습니다.");
    }

    return categorys.stream()
        .map(category -> new CategoryResponseDto(
            category.getCategoryName()
        ))
        .collect(Collectors.toList());
  }

  /**
   * 카테고리 삭제
   *
   * @param categoryId
   */
  @Transactional
  public void deleteCategory(UUID categoryId) {
    // [예외1] - 존재하지 않는 카테고리
    CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(()
        -> new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 카테고리입니다."));

    category.setDeletionStatus(DeletionStatus.DELETED);
  }
}
