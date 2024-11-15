package com.sparta.peopleoff.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

  PLACED("주문 접수"),
  PREPARING("준비중"),
  DELIVERING("배달중"),
  DELIVERED("배달 완료"),
  CANCELED("주문 취소");

  private final String description;
}
