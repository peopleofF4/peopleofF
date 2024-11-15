package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResErrorCode implements ResCodeIfs {

  //USER
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), "401", "Login failed"),

  // Security 권한 에러
  ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "403", "Not authorized"),
  ;

  private final Integer httpStatusCode;   // 아래와 상응하는 HttpStatusCode

  private final String resCode;    // Internal ErrorCode

  private final String description;


}
