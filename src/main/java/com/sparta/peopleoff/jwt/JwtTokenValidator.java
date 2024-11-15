package com.sparta.peopleoff.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import com.sparta.peopleoff.common.rescode.TokenErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j(topic = "Jwt Validation")
public class JwtTokenValidator {

  @Value("${jwt.secret}") // SecretKey
  private String secretKey;

  private Key key;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // header 에서 AccessToken 가져오기
  public String getJwtAccessTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(JwtTokenProvider.ACCESS_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(
        JwtTokenProvider.BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // header 에서 RefreshToken 가져오기
  public String getJwtRefreshTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(JwtTokenProvider.REFRESH_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(
        JwtTokenProvider.BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token, HttpServletResponse response) throws IOException {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;

    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
      setTokenErrorResponse(response, TokenErrorCode.INVALID_TOKEN,
          HttpServletResponse.SC_UNAUTHORIZED);

    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
      setTokenErrorResponse(response, TokenErrorCode.EXPIRED_TOKEN,
          HttpServletResponse.SC_UNAUTHORIZED);

    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
      setTokenErrorResponse(response, TokenErrorCode.UNSUPPORTED_TOKEN,
          HttpServletResponse.SC_UNAUTHORIZED);

    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
      setTokenErrorResponse(response, TokenErrorCode.INVALID_TOKEN,
          HttpServletResponse.SC_BAD_REQUEST);
    }
    return false;
  }

  private void setTokenErrorResponse(HttpServletResponse response, ResCodeIfs tokenErrorCode,
      int statusCode) throws IOException {

    response.setStatus(statusCode);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiResponse<Object> commonResponse = ApiResponse.ERROR(tokenErrorCode);

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);

  }

}
