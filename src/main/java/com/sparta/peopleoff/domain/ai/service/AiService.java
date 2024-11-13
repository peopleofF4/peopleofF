package com.sparta.peopleoff.domain.ai.service;

import com.sparta.peopleoff.domain.ai.dto.AiResponseDto;
import com.sparta.peopleoff.domain.ai.dto.GeminiRequestDto;
import com.sparta.peopleoff.domain.ai.dto.GeminiResponseDto;
import com.sparta.peopleoff.domain.ai.entity.AiEntity;
import com.sparta.peopleoff.domain.ai.repository.AiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiRepository aiRepository;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public AiResponseDto getContents(UUID menuId, String prompt) {
        // [예외 1] - 권한 OWNER
        // [예외 2] - 글자 수 1~255
        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        GeminiRequestDto request = new GeminiRequestDto(prompt);
        GeminiResponseDto response = restTemplate.postForObject(requestUrl, request, GeminiResponseDto.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        AiEntity aiEntity = new AiEntity(prompt, message);

        aiRepository.save(aiEntity);

        AiResponseDto aiResponseDto = new AiResponseDto(aiEntity.getId(), aiEntity.getAiResponse());

        return aiResponseDto;
    }
}
