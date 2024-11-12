package com.sparta.peopleoff.common.apiresponse;

import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * API 공통 spec
 * */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {

  private Result result;

  @Valid
  T body;

  public static <T> ApiResponse<T> OK(T data) {
    ApiResponse<T> apiResponse = new ApiResponse<>();
    apiResponse.result = Result.OK();
    apiResponse.body = data;
    return apiResponse;
  }

  // RestControllerAdvice에서 사용하면 될듯
  public static ApiResponse<Object> ERROR(ResCodeIfs resCodeIfs) {
    ApiResponse<Object> apiResponse = new ApiResponse<>();
    apiResponse.result = Result.ERROR(resCodeIfs);
    return apiResponse;
  }

  public static ApiResponse<Object> ERROR(ResCodeIfs resCodeIfs, String errorDescription) {
    ApiResponse<Object> apiResponse = new ApiResponse<>();
    apiResponse.result = Result.ERROR(resCodeIfs, errorDescription);
    return apiResponse;
  }

}
