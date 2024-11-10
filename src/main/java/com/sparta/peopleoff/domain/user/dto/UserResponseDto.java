package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.domain.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String UserName;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.role = user.getRole();
    }

    public UserResponseDto(Long id, String nickName, String email,
                           String phoneNumber, String address, String role) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public UserResponseDto(Long id, String userName, String nickName, String email,
                           String phoneNumber, String address, String role) {
        this.id = id;
        this.UserName = userName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }
}
