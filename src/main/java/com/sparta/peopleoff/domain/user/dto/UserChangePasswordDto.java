package com.sparta.peopleoff.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserChangePasswordDto {

  private String password;

  @Size(min = 8, max = 15, message = "8자 이상, 15자 이하로 입력해야 합니다.")
  @Pattern(regexp = "^[\\p{Alnum}\\p{Punct}]+$") // 알파벳 대소문자, 숫자, 그리고 모든 특수문자가 허용되는 유효성 검사
  private String newPassword;

}
