package com.sparta.peopleoff.domain.store.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreStatus {

  REGISTRATION_PENDING("등록 보류"),
  REGISTRATION_ACCEPTED("등록 승인"),
  REGISTRATION_REJECTED("등록 거부"),

  DELETED_PENDING("삭제 보류"),
  DELETED_ACCEPTED("삭제 승인"),
  DELETED_REJECTED("삭제 거부");


  private final String description;
}
