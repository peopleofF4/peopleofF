package com.sparta.peopleoff.domain.user.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UpdateResponseDto {
    private Timestamp updateAt;
    private String updateBy;
}
