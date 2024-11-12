package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.security.UserDetailsImpl;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

  private String userName;

  private String nickName;

  private String email;

  private String phoneNumber;

  private String address;

  public UserInfoResponseDto(UserDetailsImpl userDetails) {
    this.userName = userDetails.getUsername();
    this.nickName = userDetails.getUser().getNickName();
    this.email = userDetails.getUser().getEmail();
    this.phoneNumber = userDetails.getUser().getPhoneNumber();
    this.address = userDetails.getUser().getAddress();
  }

}
