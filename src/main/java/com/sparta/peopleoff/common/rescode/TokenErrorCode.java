package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * Token의 경우 2000번대 에러코드 사용
 * */
@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ResCodeIfs {
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "2000", "Invalid Token"),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "2001", "Token has expired"),
  TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "2002", "Unknown Token Error"),
  AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "2003",
      "Authorization Token Not Found"),
  ;

  // 변형이 일어나면 안되니 모두 final로 선언해 변경되지 않는 값으로 초기화.
  private final Integer httpStatusCode;

  private final String resCode;

  private final String description;

}
