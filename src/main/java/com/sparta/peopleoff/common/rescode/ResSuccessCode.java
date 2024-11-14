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
  USER_DELETED(HttpStatus.OK.value(), "200", "user successfully deleted"),
  USER_UPDATED(HttpStatus.OK.value(), "200", "user successfully updated"),
  PASSWORD_UPDATED(HttpStatus.OK.value(), "200", "password successfully updated"),

  // TOKEN
  ACCESS_TOKEN_GENERATED(HttpStatus.CREATED.value(), "201", "AccessToken is generated");

  private final Integer httpStatusCode;   // 아래와 상응하는 HttpStatusCode

  private final String resCode;    // Internal ErrorCode

  private final String description;


}
