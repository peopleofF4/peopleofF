package com.sparta.peopleoff.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {

  @Size(min = 4, max = 10, message = "4자 이상, 10자 이하로 입력해야 합니다.")
  @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자와 숫자만 사용할 수 있습니다.")
  private String userName;

  @Size(min = 8, max = 15, message = "8자 이상, 15자 이하로 입력해야 합니다.")
  @Pattern(regexp = "^[\\p{Alnum}\\p{Punct}]+$") // 알파벳 대소문자, 숫자, 그리고 모든 특수문자가 허용되는 유효성 검사
  private String password;

  @NotBlank
  private String nickName;

  @NotBlank
  private String email;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String address;

}
