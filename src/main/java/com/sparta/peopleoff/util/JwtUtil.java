package com.sparta.peopleoff.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.common.rescode.ResCodeIfs;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.common.rescode.TokenErrorCode;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.security.refreshtoken.entity.RefreshTokenEntity;
import com.sparta.peopleoff.security.refreshtoken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

  // Header KEY 값
  public static final String ACCESS_HEADER = "Authorization";
  public static final String REFRESH_HEADER = "Authorization-refresh";
  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  @Value("${jwt.secret}") // SecretKey
  private String secretKey;

  private Key key;

  @Value("${jwt.access.expiration}")
  private long accessTokenValidityInSeconds;

  @Value("${jwt.refresh.expiration}")
  private long refreshTokenValidityInSeconds;

  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // AccessToken 토큰 생성
  public String createAccessToken(String username, UserRole role) {

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username) // 사용자 식별자값(email)
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(
                new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000)) // 만료 시간
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  // RefreshToken 토큰 생성
  public String createRefreshToken(String username, UserRole role) {

    String refreshToken =
        Jwts.builder()
            .setSubject(username) // 사용자 식별자값(email)
            .claim(AUTHORIZATION_KEY, role) // 사용자 권한
            .setExpiration(
                new Date(
                    System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000)) // 만료 시간
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발급일
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();

    // TODO 여기서 쿼리를 계속 한번씩 날려 userEntity를 가져와야 된다. vs id를 claim값에 넣으려면 그때도 email로 userEntity 찾아와 넣어야된다.
    saveRefreshToken(username, refreshToken);

    return BEARER_PREFIX + refreshToken;
  }

  // refreshToken 테이블에 저장
  private void saveRefreshToken(String username, String refreshToken) {

    UserEntity user = userRepository.findByEmail(username)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.NULL_POINT, "Not Found User"));

    //TODO 한번에 save 메서드로 처리하게 하기

    refreshTokenRepository.findByUser(user).ifPresentOrElse(
        refreshTokenEntity -> {
          // refreshtoken 값이 있을 때 Update
          refreshTokenEntity.updateRefreshToken(refreshToken, user);
          refreshTokenRepository.save(refreshTokenEntity);
        },
        () -> {
          // DB에 refresh토큰 없어서 새로운 객체 만들어서 저장
          RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
          refreshTokenEntity.updateRefreshToken(refreshToken, user);
          refreshTokenRepository.save(refreshTokenEntity);
        });

    //refreshTokenRepository.save(refreshTokenEntity);

  }

  @Transactional(readOnly = true)
  public void reIssueAccessToken(HttpServletResponse response, String refreshToken)
      throws IOException {
    Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(
        refreshToken);

    if (refreshTokenEntity.isPresent()) {
      UserEntity user = refreshTokenEntity.get().getUser();  // 여기서 Lazy loading이 되야된다.
      System.out.println("reIssueAccessToken있고 user lazyLoding 확인" + user);
      sendAccessToken(response, createAccessToken(user.getEmail(), user.getRole()));
    }

  }

  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,
      String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
  }

  public void sendAccessToken(HttpServletResponse response, String accessToken) throws IOException {

    setAccessTokenHeader(response, accessToken);

    ApiResponse<Void> commonResponse = ApiResponse.OK(ResSuccessCode.ACCESS_TOKEN_GENERATED);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);
  }

  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(ACCESS_HEADER, accessToken);
  }


  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(REFRESH_HEADER, refreshToken);
  }

  // header 에서 AccessToken 가져오기
  public String getJwtAccessTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(ACCESS_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // header 에서 RefreshToken 가져오기
  public String getJwtRefreshTokenFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader(REFRESH_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰 검증
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
    response.setContentType("application/json");

    ApiResponse<Object> commonResponse = ApiResponse.ERROR(tokenErrorCode);

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);

  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }


}

