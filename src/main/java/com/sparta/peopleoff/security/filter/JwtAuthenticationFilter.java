package com.sparta.peopleoff.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.domain.user.dto.LoginRequestDto;
import com.sparta.peopleoff.domain.user.entity.UserRole;
import com.sparta.peopleoff.security.UserDetailsImpl;
import com.sparta.peopleoff.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
    setFilterProcessesUrl("/api/v1/users/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {

      LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
          LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getEmail(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {
    String username = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail();
    UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

    String accessToken = jwtUtil.createAccessToken(username, role);
    String refreshToken = jwtUtil.createRefreshToken(username, role);

    log.info("Access_Token : {}", accessToken);
    log.info("Refresh_Token : {}", refreshToken);

    jwtUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    ApiResponse<Void> commonResponse = ApiResponse.OK(ResSuccessCode.LOGIN_SUCCESS);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {

    //ResponseEntity 형식을 맞춰 바디에 에러 정보 작성
    ApiResponse<Object> errorResponse = ApiResponse.ERROR(ResErrorCode.LOGIN_FAILED);

    // 일반적인 인증 실패 처리
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
  }
}