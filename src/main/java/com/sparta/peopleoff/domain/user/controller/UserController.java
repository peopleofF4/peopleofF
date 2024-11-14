package com.sparta.peopleoff.domain.user.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.UserChangePasswordDto;
import com.sparta.peopleoff.domain.user.dto.UserInfoResponseDto;
import com.sparta.peopleoff.domain.user.dto.UserSignUpRequestDto;
import com.sparta.peopleoff.domain.user.dto.UserUpdateRequestDto;
import com.sparta.peopleoff.domain.user.service.UserServiceImpl;
import com.sparta.peopleoff.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserServiceImpl userServiceImpl;

  /**
   * 회원 가입 API
   *
   * @param userSignUpRequestDto
   * @return
   */
  @PostMapping("/users/signup")
  public ResponseEntity<ApiResponse<Void>> signUp(
      @Valid
      @RequestBody UserSignUpRequestDto userSignUpRequestDto) {

    userServiceImpl.signUp(userSignUpRequestDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.SIGNUP_SUCCESS))
        ;
  }

  /**
   * 회원 상세 정보 API
   *
   * @param user
   * @return
   */
  @GetMapping("/users/info")
  public ResponseEntity<ApiResponse<UserInfoResponseDto>> getUserInfo(
      @AuthenticationPrincipal UserDetailsImpl user) {

    UserInfoResponseDto response = userServiceImpl.getUserInfo(user);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(response));
  }

  /**
   * 회원 정보 수정 API (정보가 null로 오는거는 없다고 가정?)
   *
   * @param userId
   * @param userUpdateInfoDto
   * @return
   */
  @PutMapping("users/{userId}")
  public ResponseEntity<ApiResponse<Void>> updateUserInfo(@PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequestDto userUpdateInfoDto) {

    userServiceImpl.updateUserInfo(userId, userUpdateInfoDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.USER_UPDATED));
  }


  /**
   * 회원 삭제 API
   *
   * @param userId
   * @return
   */
  @DeleteMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {

    userServiceImpl.deleteUser(userId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.USER_DELETED));
  }

  /**
   * // 비밀번호 변경 API
   *
   * @param userChangePasswordDto
   * @param userDetails
   * @return
   */
  @PutMapping("/users/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(
      @RequestBody @Valid UserChangePasswordDto userChangePasswordDto
      , @AuthenticationPrincipal UserDetailsImpl userDetails) {

    userServiceImpl.changePassword(userChangePasswordDto, userDetails);

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResSuccessCode.PASSWORD_UPDATED));
  }

}
