package com.sparta.peopleoff.domain.user.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String nickname;
    private String email;
    private String phoneNumber;
    private String address;
}
