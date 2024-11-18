package com.sparta.peopleoff.domain.ai.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.domain.ai.dto.AiRequestDto;
import com.sparta.peopleoff.domain.ai.service.AiService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AiController {

  private final AiService aiService;


  @PreAuthorize("hasRole('OWNER')")
  @PostMapping("/menu/{menuId}/ai/chat")
  public ResponseEntity<ApiResponse<?>> gemini(@PathVariable UUID menuId,
      @Valid @RequestBody AiRequestDto requestDto) {
    try {
      return ResponseEntity.status(HttpStatus.OK).
          body(ApiResponse.OK(aiService.getContents(menuId, requestDto.getAiRequest())));
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(e.getMessage()));
    }
  }

}
