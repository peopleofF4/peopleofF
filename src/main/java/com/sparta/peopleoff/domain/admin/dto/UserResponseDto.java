package com.sparta.peopleoff.domain.admin.dto;

import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {

  private Long id;
  private String UserName;
  private String nickName;
  private String email;
  private String phoneNumber;
  private String address;
  private UserRole role;


  public UserResponseDto(Long id, String userName, String nickName, String email,
      String phoneNumber, String address, UserRole userRole) {
    this.id = id;
    this.UserName = userName;
    this.nickName = nickName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.role = userRole;
  }
}
