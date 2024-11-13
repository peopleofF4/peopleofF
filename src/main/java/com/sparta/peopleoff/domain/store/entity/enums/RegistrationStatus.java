package com.sparta.peopleoff.domain.store.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistrationStatus {
  PENDING("보류"),
  ACCEPTED("수락"),
  REJECTED("거절");

  private final String description;
}
