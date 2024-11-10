package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 *
 * 기본 Response 코드
 * */

@Getter
@AllArgsConstructor
public enum ResBasicCode implements ResCodeIfs {

  OK(HttpStatus.OK.value(), "200", "Success"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400", "Bad Request"),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "UnKnone Server Error"),
  NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "512", "Null point");


  private final Integer httpStatusCode;   // 아래와 상응하는 HttpStatusCode

  private final String resCode;    // Internal ErrorCode

  private final String description;


}
