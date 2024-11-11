package com.sparta.peopleoff.security.filter;

import com.sparta.peopleoff.security.UserDetailsServiceImpl;
import com.sparta.peopleoff.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("JwtAuthorizationFilter doFilterInternal 메서드 들어옴");

    String refreshToken = jwtUtil.getJwtRefreshTokenFromHeader(req);

    if (StringUtils.hasText(refreshToken)) {

      if (jwtUtil.validateToken(refreshToken)) {
        System.out.println("refreshToken = " + refreshToken);
        checkRefreshTokenAndReIssueAccessToken(res, refreshToken);
        return;
      }

    }

    log.info("JwtAuthorizationFilter doFilterInternal 메서드 checkAccessTokenAndAuthentication 바로전");

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

    String accessToken = jwtUtil.getJwtAccessTokenFromHeader(request);

    if (StringUtils.hasText(accessToken)) {

      if (jwtUtil.validateToken(accessToken)) {
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);

        log.info("checkAccessTokenAndAuthentication 메서드 실행 후 contextHolder에 set");
        setAuthentication(claims.getSubject());
      }

    }

    filterChain.doFilter(request, response);
  }

  private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
      String refreshToken) throws IOException {

    jwtUtil.reIssueAccessToken(response, refreshToken);
  }
}
