package com.sparta.peopleoff.domain.category.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성
     * @param categoryRequestDto
     * @return
     */
    @PostMapping
    private ResponseEntity<ApiResponse<String>> createCategory(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(/*userDetails.getUser(), */categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK("카테고리 생성을 성공했습니다."));
    }

    /**
     * 카테고리 수정
     * @param categoryId
     * @param categoryRequestDto
     * @return
     */
    @PutMapping("/{categoryId}")
    private ResponseEntity<ApiResponse<String>> updateCategory(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.updateCategory(/*userDetails.getUser(), */categoryId, categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK("카테고리 수정을 성공했습니다."));
    }
}
