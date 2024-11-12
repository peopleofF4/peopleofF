package com.sparta.peopleoff.domain.user.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerApproveRegistrationStatus {

  ACCEPTED("승인"),
  REJECTED("거부"),
  HOLD("보류"),
  NONE("없음");

  private final String description;
}
