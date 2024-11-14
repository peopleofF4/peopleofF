package com.sparta.peopleoff.domain.user.dto;

import com.sparta.peopleoff.domain.user.entity.UserEntity;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class UpdateResponseDto {
    private LocalDateTime updateAt;
    private String updateBy;

    public UpdateResponseDto(UserEntity user) {
        this.updateAt = user.getUpdatedAt();
        this.updateBy = user.getUpdatedBy();
    }
}
