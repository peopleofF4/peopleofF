package com.sparta.peopleoff.domain.store.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistrationStatus {
  PENDING("보류"),
  ACCEPTED("승인"),
  REJECTED("거절"),
  NONE("없음");

  private final String description;
}
