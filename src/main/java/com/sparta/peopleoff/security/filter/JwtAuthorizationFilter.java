package com.sparta.peopleoff.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
import com.sparta.peopleoff.common.rescode.TokenErrorCode;
import com.sparta.peopleoff.jwt.JwtTokenProvider;
import com.sparta.peopleoff.jwt.JwtTokenValidator;
import com.sparta.peopleoff.jwt.refreshtoken.service.RefreshTokenService;
import com.sparta.peopleoff.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final RefreshTokenService refreshTokenService;
  private final JwtTokenValidator jwtTokenValidator;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
      throws ServletException, IOException {

    String refreshToken = jwtTokenValidator.getJwtRefreshTokenFromHeader(req);

    if (StringUtils.hasText(refreshToken) && jwtTokenValidator.validateToken(refreshToken, res)) {
      checkRefreshTokenAndReIssueAccessToken(res, refreshToken);
      return;
    }

    checkAccessTokenAndAuthentication(req, res, filterChain);
  }

  // 인증 처리
  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }

  // 인증 객체 생성
  private Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  private void checkAccessTokenAndAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String accessToken = jwtTokenValidator.getJwtAccessTokenFromHeader(request);

    if (StringUtils.hasText(accessToken) && jwtTokenValidator.validateToken(accessToken,
        response)) {

      Claims claims = JwtTokenProvider.getUserInfoFromToken(accessToken);

      if (claims.get(JwtTokenProvider.REFRESH_HEADER).equals(JwtTokenProvider.REFRESH_TOKEN)) {
        putRefreshTokenInAuthorization(response);
      }

      setAuthentication(claims.getSubject());

    }

    filterChain.doFilter(request, response);
  }

  private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
      String refreshToken) throws IOException {

    refreshTokenService.reIssueAccessToken(response, refreshToken);

    ApiResponse<Void> commonResponse = ApiResponse.OK(ResSuccessCode.ACCESS_TOKEN_GENERATED);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);
  }

  private void putRefreshTokenInAuthorization(HttpServletResponse response) throws IOException {
    ApiResponse<Object> errorResponse = ApiResponse.ERROR(TokenErrorCode.NOT_ACCESS_TOKEN);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);

  }
}



