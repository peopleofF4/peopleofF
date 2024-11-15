package com.sparta.peopleoff.exception.exceptionhandler;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CustomApiExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE) // 최우선 처리
public class CustomApiExceptionHandler {

  @ExceptionHandler(value = CustomApiException.class)
  public ResponseEntity<ApiResponse<Object>> apiException(CustomApiException customApiException) {

    // 왜 stacktrace가 가능하냐면 RuntimeException을 상속받았기 때문
    log.error("", customApiException);

    ResCodeIfs errorCode = customApiException.getErrorCode();

    return ResponseEntity
        .status(errorCode.getHttpStatusCode())
        .body(
            ApiResponse.ERROR(errorCode, customApiException.getErrorDescription()) // 이 방식이 제일 좋다.
        );

  }


}
