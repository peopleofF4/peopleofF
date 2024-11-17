package com.sparta.peopleoff.exception.exceptionhandler;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE) // 가장 마지막에 실행 적용 (기본값이지만 볼 수 있게 명시적 표현)
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiResponse<Object>> exception(Exception exception) {

    log.error("", exception);   // stackTrace 찍는곳

    return ResponseEntity
        .status(500)
        .body(
            ApiResponse.ERROR(ResBasicCode.SERVER_ERROR, exception.getMessage())
        );
  }
}
