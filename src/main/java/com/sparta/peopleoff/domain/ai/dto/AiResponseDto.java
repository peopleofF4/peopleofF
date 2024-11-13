package com.sparta.peopleoff.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AiResponseDto {

    private UUID id;

    private String aiResponse;

    private String menuName;

    public AiResponseDto(UUID id, String aiResponse) {
        this.id = id;
        this.aiResponse = aiResponse;
    }
}
