package com.sparta.peopleoff.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RegistrationStatus {

    PENDING("보류"),
    ACCEPTED("수락"),
    REJECTED("거절");

    private final String description;
}
