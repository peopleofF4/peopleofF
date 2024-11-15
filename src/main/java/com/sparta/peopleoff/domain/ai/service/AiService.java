package com.sparta.peopleoff.domain.ai.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.ai.dto.AiResponseDto;
import com.sparta.peopleoff.domain.ai.dto.GeminiRequestDto;
import com.sparta.peopleoff.domain.ai.dto.GeminiResponseDto;
import com.sparta.peopleoff.domain.ai.entity.AiEntity;
import com.sparta.peopleoff.domain.ai.repository.AiRepository;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.repository.MenuRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiRepository aiRepository;
    private final MenuRepository menuRepository;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public AiResponseDto getContents(UUID menuId, String prompt) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        GeminiRequestDto request = new GeminiRequestDto(prompt);
        GeminiResponseDto response = restTemplate.postForObject(requestUrl, request, GeminiResponseDto.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        MenuEntity menu = menuRepository.findById(menuId).orElseThrow(() ->
                new CustomApiException(ResBasicCode.BAD_REQUEST, "존재하지 않는 메뉴입니다."));

        AiEntity aiEntity = new AiEntity(prompt, message, menu);

        aiRepository.save(aiEntity);

        AiResponseDto aiResponseDto = new AiResponseDto(aiEntity.getId(), aiEntity.getAiResponse(), menu.getMenuName());

        return aiResponseDto;
    }

    private void checkOwnerAuthority(UserEntity user) {
        if (!(UserRole.OWNER).equals(user.getRole())) {
            throw new CustomApiException(ResBasicCode.BAD_REQUEST, "Owner 권한으로 접근할 수 있읍니다.");
        }
    }
}
