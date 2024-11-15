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
    public void createCategory(/*User user, */CategoryRequestDto categoryRequestDto) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        CategoryEntity category = new CategoryEntity(categoryRequestDto);
        categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
     * @param categoryId
     * @param categoryRequestDto
     */
    @Transactional
    public void updateCategory(/*User user, */UUID categoryId, CategoryRequestDto categoryRequestDto) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        // [예외2] - 존재하지 않는 categoryId
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 카테고리입니다."));

        category.setCategoryName(category.getCategoryName());

        categoryRepository.save(category);
    }

//    private void checkAdminAuthority(User user) {
//        private void checkAdminAuthority(User user) {
//            if (!"ADMIN".equals(user.getUserRole())) { // Todo: ADMIN은 나중에 Enum으로 유저role 생기면 바꾸기
//                throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Admin권한으로 접근할 수 있읍니다.");
//            }
//        }
//    }
}
