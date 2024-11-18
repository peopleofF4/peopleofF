package com.sparta.peopleoff.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AiRequestDto {

    @NotBlank(message = "물어볼 내용을 입력해주세요.")
    @Size(min = 1, max = 255, message = "응답 글자 수는 최대 255글자 입니다.")
    private String aiRequest;
}
