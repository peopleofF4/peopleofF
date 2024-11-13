package com.sparta.peopleoff.domain.ai.service;

import com.sparta.peopleoff.domain.ai.dto.AiRequestDto;
import com.sparta.peopleoff.domain.ai.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiService {

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getContents(String prompt) {
        // [예외 1] - 권한 OWNER
        // [예외 2] - 글자 수 1~255
        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

//        AiRequestDto request = new AiRequestDto(prompt);
//        AiResponseDto response = restTemplate.postForObject(requestUrl, request, AiResponseDto.class);
//        // 헤더에 requestUrl 넣기

        AiRequestDto request = new AiRequestDto(prompt);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json"); // 필요에 따라 Content-Type 설정
        headers.set("Authorization", "Bearer " + geminiApiKey); // API 키를 Authorization 헤더로 설정

        // HttpEntity 생성
        HttpEntity<AiRequestDto> entity = new HttpEntity<>(request, headers);

        // POST 요청 보내기
        AiResponseDto response = restTemplate.postForObject(requestUrl, entity, AiResponseDto.class);

        // 응답 처리
        if (response == null || response.getCandidates().isEmpty()) {
            throw new RuntimeException("No response from Gemini API");
        }

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        return message;
    }
}
