package com.sparta.peopleoff.domain.category.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/v1")
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * 카테고리 생성
   *
   * @param categoryRequestDto
   * @return
   */
  @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
  @PostMapping("/category")
  public ResponseEntity<ApiResponse<Void>> createCategory(
      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    categoryService.createCategory(categoryRequestDto);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.CATEGORY_CREATED));
  }

  /**
   * 카테고리 수정
   *
   * @param categoryId
   * @param categoryRequestDto
   * @return
   */
  @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
  @PutMapping("/category/{categoryId}")
  public ResponseEntity<ApiResponse<Void>> updateCategory(
      @PathVariable UUID categoryId,
      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    categoryService.updateCategory(categoryId, categoryRequestDto);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.CATEGORY_UPDATED));
  }
}
