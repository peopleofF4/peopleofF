package com.sparta.peopleoff.domain.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

  SUCCESS("결제완료"),
  FAILED("결제취소");

  private final String description;
}
