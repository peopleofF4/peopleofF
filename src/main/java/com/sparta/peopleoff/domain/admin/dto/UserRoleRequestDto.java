package com.sparta.peopleoff.domain.admin.dto;

import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRoleRequestDto {

  @NotBlank
  private UserRole userRole;
}
