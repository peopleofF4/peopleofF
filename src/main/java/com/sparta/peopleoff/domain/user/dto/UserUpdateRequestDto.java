package com.sparta.peopleoff.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

  private String userName;

  @NotBlank(message = "비밀번호는 필수 값입니다.")
  private String password;

  private String nickName;

  private String phoneNumber;

  private String address;
  
}
