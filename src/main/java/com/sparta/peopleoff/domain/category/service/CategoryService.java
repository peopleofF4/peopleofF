package com.sparta.peopleoff.domain.category.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     * @param categoryRequestDto
     */
    @Transactional
    public void createCategory(CategoryRequestDto categoryRequestDto) {
        CategoryEntity category = new CategoryEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
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
}
