package com.sparta.peopleoff.exception.exceptionhandler;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ValidExceptionHandler")
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE + 1)
public class ValidExceptionHandler {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Object> exception(MethodArgumentNotValidException exception) {

    log.error("", exception);   // stackTrace 찍는곳

    List<String> errors = new ArrayList<>();
    exception.getBindingResult().getFieldErrors().forEach(error ->
        errors.add(error.getField() + " : " + error.getDefaultMessage())
    );

    return ResponseEntity
        .status(500)
        .body(
            ApiResponse.ERROR(ResBasicCode.BAD_REQUEST, errors)
        );
  }

}
