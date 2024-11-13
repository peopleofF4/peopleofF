package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.domain.user.entity.UserEntity;
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

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.role = user.getRole();
    }

    public UserResponseDto(Long id, String nickName, String email,
                           String phoneNumber, String address, UserRole userRole) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = userRole;
    }

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
