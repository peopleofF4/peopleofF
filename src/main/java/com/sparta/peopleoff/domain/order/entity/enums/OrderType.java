package com.sparta.peopleoff.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

  ON_LINE("비대면 접수"),
  OFF_LINE("대면 접수");

  private final String description;
}
