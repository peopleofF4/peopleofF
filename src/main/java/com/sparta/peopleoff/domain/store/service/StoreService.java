package com.sparta.peopleoff.domain.store.service;

import com.sparta.peopleoff.domain.store.dto.StoreGetResponseDto;
import com.sparta.peopleoff.domain.store.dto.StorePostRequestDto;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StoreService {

  void registerStore(StorePostRequestDto storeRequestDto, UserEntity user);

  StoreGetResponseDto getStoreById(UUID storeId);

  List<StoreGetResponseDto> getAllStores(Pageable pageable);

  void updateStore(UUID storeId, StorePutRequestDto storeUpdateRequestDto);

  void deleteStore(UUID storeId);

  List<StoreGetResponseDto> searchStores(String keyword, Pageable pageable);

  double getAverageRating(UUID storeId);

  List<StoreGetResponseDto> getStoresByOwner(UserEntity owner);
}
