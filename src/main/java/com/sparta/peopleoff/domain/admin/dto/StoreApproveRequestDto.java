package com.sparta.peopleoff.domain.admin.dto;

import com.sparta.peopleoff.domain.store.entity.enums.StoreStatus;
import lombok.Getter;

@Getter
public class StoreApproveRequestDto {

  private StoreStatus storeStatus;

}
