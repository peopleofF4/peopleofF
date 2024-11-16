package com.sparta.peopleoff.domain.menu.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuStatus {

  ON_SALE("판매중"),
  SOLD_OUT("하루품절"),
  HIDING("숨김");

  private final String description;
}
