package com.sparta.peopleoff.domain.store.dto;

import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import lombok.Getter;

@Getter
public class StoreGetResponseDto {

  private String storeName;
  private String storeAddress;
  private String storePhoneNumber;
  private String businessNumber;
  private int minimumOrderPrice;
  private String categoryName;
  private double averageRating;

  public StoreGetResponseDto(StoreEntity store) {
    this.storeName = store.getStoreName();
    this.storeAddress = store.getStoreAddress();
    this.storePhoneNumber = store.getStorePhoneNumber();
    this.businessNumber = store.getBusinessNumber();
    this.minimumOrderPrice = store.getMinimumOrderPrice();
    this.categoryName = store.getCategory().getCategoryName();
    this.averageRating = calculateAverageRating(store);
  }

  private double calculateAverageRating(StoreEntity store) {
    if (store.getRatingCount() == 0) {
      return 0.0;
    }
    return (double) store.getTotalRating() / store.getRatingCount();
  }
}
