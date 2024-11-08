package com.sparta.peopleoff.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DeletionStatus {

  ACTIVE("활성"),
  DELETED("삭제");

  private final String description;
}
