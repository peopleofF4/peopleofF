package com.sparta.peopleoff.jwt.refreshtoken.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.jwt.JwtTokenProvider;
import com.sparta.peopleoff.jwt.refreshtoken.entity.RefreshTokenEntity;
import com.sparta.peopleoff.jwt.refreshtoken.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "RefreshTokenService")
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  // refreshToken 테이블에 저장
  public void saveRefreshToken(String username, String refreshToken) {

    String replaceRefreshToken = refreshToken.replace(JwtTokenProvider.BEARER_PREFIX, "");

    UserEntity user = userRepository.findByEmail(username)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.NULL_POINT, "Not Found User"));

    //TODO 한번에 save 메서드로 처리하게 하기

    refreshTokenRepository.findByUser(user).ifPresentOrElse(
        refreshTokenEntity -> {
          // refreshtoken 값이 있을 때 Update
          refreshTokenEntity.updateRefreshToken(replaceRefreshToken, user);
          refreshTokenRepository.save(refreshTokenEntity);
        },
        () -> {
          // DB에 refresh토큰 없어서 새로운 객체 만들어서 저장
          RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
          refreshTokenEntity.updateRefreshToken(replaceRefreshToken, user);
          refreshTokenRepository.save(refreshTokenEntity);
        });
  }

  @Transactional(readOnly = true)
  public void reIssueAccessToken(HttpServletResponse response, String refreshToken)
      throws IOException {
    Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(
        refreshToken);

    if (refreshTokenEntity.isPresent()) {
      UserEntity user = refreshTokenEntity.get().getUser();  // 여기서 Lazy loading 확인.
      response.setHeader(JwtTokenProvider.ACCESS_HEADER,
          JwtTokenProvider.createAccessToken(user.getEmail(), user.getRole()));
    }
  }

}
