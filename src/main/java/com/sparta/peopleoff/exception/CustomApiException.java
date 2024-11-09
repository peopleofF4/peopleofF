package com.sparta.peopleoff.exception;

import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException implements CustomApiExceptionIfs {

  private final ResCodeIfs errorCode;

  private final String errorDescription;

  public CustomApiException(ResCodeIfs errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.errorDescription = errorCode.getDescription();
  }

  public CustomApiException(ResCodeIfs errorCode, String errorDescription) {
    super(errorDescription);
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }
}
