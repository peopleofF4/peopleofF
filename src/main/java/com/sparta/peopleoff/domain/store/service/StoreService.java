package com.sparta.peopleoff.domain.store.service;

import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  public StoreGetResponseDto getStoreById(UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 가게가 존재하지 않습니다."));
    return new StoreGetResponseDto(store);
  }

  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> getAllStores() {
    return storeRepository.findAll().stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }
}
