package com.sparta.peopleoff.domain.store.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.category.repository.CategoryRepository;
import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.exception.CustomApiException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

  private final StoreRepository storeRepository;
  private final CategoryRepository categoryRepository;

  public StoreServiceImpl(StoreRepository storeRepository, CategoryRepository categoryRepository) {
    this.storeRepository = storeRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  @Transactional
  public StoreEntity registerStore(StorePostRequestDto storeRequestDto, UserEntity user) {
    CategoryEntity category = categoryRepository.findByCategoryName(
            storeRequestDto.getCategoryName())
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다."));

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

  @Override
  @Transactional(readOnly = true)
  public StoreGetResponseDto getStoreById(UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 ID의 가게가 존재하지 않습니다."));
    return new StoreGetResponseDto(store);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> getAllStores(String sortBy, String sortDirection, int pageSize,
      int page) {
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    Pageable pageable = PageRequest.of(page, pageSize,
        Sort.by(direction, sortBy.equals("updatedAt") ? "updatedAt" : "createdAt"));

    Page<StoreEntity> storePage = storeRepository.findByDeletionStatus(DeletionStatus.ACTIVE,
        pageable);

    return storePage.stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateStore(UUID storeId, StorePutRequestDto storeUpdateRequestDto) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 가게를 찾을 수 없습니다."));

    CategoryEntity category = categoryRepository.findByCategoryName(
            storeUpdateRequestDto.getCategoryName())
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다."));

    store.update(storeUpdateRequestDto, category);
  }

  @Override
  @Transactional
  public void deleteStore(UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 가게를 찾을 수 없습니다."));

    store.delete();
  }

  @Override
  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> searchStores(String keyword, String sortBy, String sortDirection,
      int pageSize, int page) {
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    Pageable pageable = PageRequest.of(page, pageSize,
        Sort.by(direction, sortBy.equals("updatedAt") ? "updatedAt" : "createdAt"));

    Page<StoreEntity> storesPage = storeRepository.findByDeletionStatusAndStoreNameContainingOrCategory_CategoryNameContainingOrStoreAddressContainingOrStorePhoneNumberContaining(
        DeletionStatus.ACTIVE, keyword, keyword, keyword, keyword, pageable
    );

    return storesPage.stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }
}