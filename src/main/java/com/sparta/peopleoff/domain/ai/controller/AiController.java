package com.sparta.peopleoff.domain.ai.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.ai.dto.AiRequestDto;
import com.sparta.peopleoff.domain.ai.dto.AiResponseDto;
import com.sparta.peopleoff.domain.ai.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class AiController {

    private final AiService aiService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> gemini(@PathVariable UUID menuId, @Valid @RequestBody AiRequestDto requestDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(aiService.getContents(menuId, requestDto.getAiRequest())));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(e.getMessage()));
        }
    }

}
