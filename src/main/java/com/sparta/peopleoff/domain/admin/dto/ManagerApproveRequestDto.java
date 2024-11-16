package com.sparta.peopleoff.domain.admin.dto;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.enums.RegistrationStatus;
import lombok.Getter;

@Getter
public class ManagerApproveRequestDto {

  private RegistrationStatus registrationStatus;

  private DeletionStatus deletionStatus;
}
