package com.sparta.peopleoff.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistrationStatus {
  PENDING("보류"),
  ACCEPTED("수락"),
  REJECTED("거절"),
  NONE("없음");

  private final String description;
}
