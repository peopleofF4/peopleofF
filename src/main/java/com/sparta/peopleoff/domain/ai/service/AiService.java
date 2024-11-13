package com.sparta.peopleoff.domain.ai.service;

import com.sparta.peopleoff.domain.ai.dto.AiRequestDto;
import com.sparta.peopleoff.domain.ai.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        String requestUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent" + "?key=" + "AIzaSyBpX38E9lu0OwI5z6NahIQBDi0DFVSeog0";

        AiRequestDto request = new AiRequestDto(prompt);
        AiResponseDto response = restTemplate.postForObject(requestUrl, request, AiResponseDto.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        return message;
    }
}
