package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.store.entity.enums.RegistrationStatus;
import lombok.Getter;

@Getter
public class ManagerApproveRequestDto {

    private RegistrationStatus registrationStatus;

    private DeletionStatus deletionStatus;
}
