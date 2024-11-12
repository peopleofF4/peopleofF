package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.service.UserService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  // 회원 가입 API
  @PostMapping("/users/signup")
  public ResponseEntity<ApiResponse<Void>> signUp(
      @Valid
      @RequestBody UserSignUpRequestDto userSignUpRequestDto) {

    userService.signUp(userSignUpRequestDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.SIGNUP_SUCCESS))
        ;
  }

  // 회원 상세 정보 API
  @GetMapping("/users/info")
  public ResponseEntity<ApiResponse<UserInfoResponseDto>> getUserInfo(
      @AuthenticationPrincipal UserDetailsImpl user) {

    UserInfoResponseDto response = userService.getUserInfo(user);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(response));
  }

}
