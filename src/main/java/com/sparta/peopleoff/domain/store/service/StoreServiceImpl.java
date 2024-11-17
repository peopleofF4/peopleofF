package com.sparta.peopleoff.domain.store.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.common.util.DeletionValidator;
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
import org.springframework.data.domain.Pageable;
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

  /**
   * 가게 등록
   *
   * @param storeRequestDto
   * @param user
   */
  @Override
  @Transactional
  public void registerStore(StorePostRequestDto storeRequestDto, UserEntity user) {
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

    storeRepository.save(newStore);
  }

  /**
   * 가게 단건 조회
   *
   * @param storeId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public StoreGetResponseDto getStoreById(UUID storeId) {
    StoreEntity store = findActiveStoreById(storeId);
    return new StoreGetResponseDto(store);
  }

  /**
   * 가게 전체 조회
   *
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> getAllStores(Pageable pageable) {
    Page<StoreEntity> storePage = storeRepository.findByDeletionStatus(DeletionStatus.ACTIVE,
        pageable);
    return storePage.stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 가게 수정
   *
   * @param storeId
   * @param storeUpdateRequestDto
   */
  @Override
  @Transactional
  public void updateStore(UUID storeId, StorePutRequestDto storeUpdateRequestDto) {
    StoreEntity store = findActiveStoreById(storeId);

    CategoryEntity category = categoryRepository.findByCategoryName(
            storeUpdateRequestDto.getCategoryName())
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다."));

    store.update(storeUpdateRequestDto, category);
  }

  /**
   * 가게 삭제 (soft-delete)
   *
   * @param storeId
   */
  @Override
  @Transactional
  public void deleteStore(UUID storeId) {
    StoreEntity store = findActiveStoreById(storeId);
    store.delete();
  }

  /**
   * 가게 검색
   *
   * @param keyword
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> searchStores(String keyword, Pageable pageable) {
    Page<StoreEntity> storesPage = storeRepository.findByDeletionStatusAndStoreNameContainingOrCategory_CategoryNameContainingOrStoreAddressContainingOrStorePhoneNumberContaining(
        DeletionStatus.ACTIVE, keyword, keyword, keyword, keyword, pageable);

    return storesPage.stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 사장님 본인 가게 조회
   *
   * @param owner
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<StoreGetResponseDto> getStoresByOwner(UserEntity owner) {
    if (!owner.getRole().name().equals("OWNER")) {
      throw new CustomApiException(ResErrorCode.FORBIDDEN, "사장님 권한이 필요합니다.");
    }

    List<StoreEntity> stores = storeRepository.findByUserAndDeletionStatus(owner,
        DeletionStatus.ACTIVE);
    return stores.stream()
        .map(StoreGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 가게별 리뷰 평점 평균값 반환
   *
   * @param storeId
   * @return
   */
  @Override
  public double getAverageRating(UUID storeId) {
    StoreEntity store = findActiveStoreById(storeId);

    // 평균 평점 계산
    long totalReviews = store.getRatingCount();
    int totalRating = store.getTotalRating();
    return totalReviews > 0 ? (double) totalRating / totalReviews : 0.0;
  }

  // 활성화된 가게 ID 조회 메서드
  private StoreEntity findActiveStoreById(UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 가게가 존재하지 않습니다."));
    DeletionValidator.validateActive(store.getDeletionStatus(), "가게");
    return store;
  }
}
