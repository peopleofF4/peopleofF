package com.sparta.peopleoff.jwt;

import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// @Transactional
@Slf4j(topic = "JwtUtil")
@Component
public class JwtTokenProvider {

  // Header KEY 값
  public static final String ACCESS_HEADER = "Authorization";
  public static final String REFRESH_HEADER = "Authorization-refresh";
  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";

  @Value("${jwt.secret}") // SecretKey
  private String secretKey;

  private static Key key;

  @Value("${jwt.access.expiration}")
  private long accessTokenValidityInSeconds;

  @Value("${jwt.refresh.expiration}")
  private long refreshTokenValidityInSeconds;

  private static long accessTokenValidate;
  private static long refreshTokenValidate;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);

    accessTokenValidate = accessTokenValidityInSeconds * 1000;
    refreshTokenValidate = refreshTokenValidityInSeconds * 1000;
  }

  // AccessToken 토큰 생성
  public static String createAccessToken(String username, UserRole role) {

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username) // 사용자 식별자값(email)
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(
                new Date(System.currentTimeMillis() + accessTokenValidate)) // 만료 시간
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
            .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
            .compact();
  }

  // RefreshToken 토큰 생성
  public static String createRefreshToken(String username, UserRole role) {

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username) // 사용자 식별자값(email)
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(
                new Date(
                    System.currentTimeMillis() + refreshTokenValidate)) // 만료 시간
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
            .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
            .compact();

  }


  // 토큰에서 사용자 정보 가져오기
  public static Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }


}

