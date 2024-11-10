package com.sparta.peopleoff.domain.category.service;

import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(/*User user, */CategoryRequestDto categoryRequestDto) {
        // [예외1] - Admin 권한 체크
//        checkAdminAuthority(user);

        CategoryEntity category = new CategoryEntity(categoryRequestDto);
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
