package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResSuccessCode implements ResCodeIfs {

  // USER
  SIGNUP_SUCCESS(HttpStatus.OK.value(), "200", "signup Success"),
  LOGIN_SUCCESS(HttpStatus.OK.value(), "200", "login Success"),

  // TOKEN
  ACCESS_TOKEN_GENERATED(HttpStatus.OK.value(), "201", "AccessToken is generated");

  private final Integer httpStatusCode;   // 아래와 상응하는 HttpStatusCode

  private final String resCode;    // Internal ErrorCode

  private final String description;


}
