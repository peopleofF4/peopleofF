package com.sparta.peopleoff.domain.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StorePutRequestDto {

  private String storeName;
  private String storeAddress;
  private String storePhoneNumber;
  private String businessNumber;
  private int minimumOrderPrice;
  private String categoryName;
}