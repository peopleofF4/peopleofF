package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import lombok.Getter;

@Getter
public class UserRoleRequestDto {
    private UserRole userRole;
}
