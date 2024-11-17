package com.sparta.peopleoff.common.util;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.exception.CustomApiException;

public class DeletionValidator {

  private DeletionValidator() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static void validateActive(DeletionStatus deletionStatus, String entityName) {
    if (deletionStatus == DeletionStatus.DELETED) {
      throw new CustomApiException(ResErrorCode.RESOURCE_DELETED,
          String.format("해당 %s는 이미 삭제된 상태입니다.", entityName));
    }
  }
}
