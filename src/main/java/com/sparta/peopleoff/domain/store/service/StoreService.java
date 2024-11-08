package com.sparta.peopleoff.domain.store.service;

import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

  private final StoreRepository storeRepository;
  private final CategoryRepository categoryRepository;

  public StoreService(StoreRepository storeRepository, CategoryRepository categoryRepository) {
    this.storeRepository = storeRepository;
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public StoreEntity registerStore(StorePostRequestDto storeRequestDto, UserEntity user) {

    CategoryEntity category = categoryRepository.findByCategoryName(
            storeRequestDto.getCategoryName())
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 이름입니다."));

    StoreEntity newStore = new StoreEntity(
        storeRequestDto.getStoreName(),
        storeRequestDto.getStoreAddress(),
        storeRequestDto.getStorePhoneNumber(),
        storeRequestDto.getBusinessNumber(),
        storeRequestDto.getMinimumOrderPrice(),
        user,
        category
    );

    return storeRepository.save(newStore);
  }
}
