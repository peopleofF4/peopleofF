package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ForbiddenErrorCode implements ResCodeIfs {

  FORBIDDEN_MANAGER(HttpStatus.FORBIDDEN.value(), "3000", "No Manager permissions."),
  FORBIDDEN_MASTER(HttpStatus.FORBIDDEN.value(), "3001", "No Master permissions."),
  FORBIDDEN_OWNER(HttpStatus.FORBIDDEN.value(), "3002", "No Owner permissions."),
  FORBIDDEN_CUSTOMER(HttpStatus.FORBIDDEN.value(), "3003", "No Customer permissions.");

  private final Integer httpStatusCode;
  private final String resCode;
  private final String description;

}
