package com.sparta.peopleoff.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletionStatus {

  ACTIVE("활성"),
  DELETED("삭제");

  private final String description;
}
