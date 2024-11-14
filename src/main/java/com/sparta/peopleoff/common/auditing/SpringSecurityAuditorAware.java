package com.sparta.peopleoff.common.auditing;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "auditorAware")
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {

    // 회원가입시 가지고 email 가지고 온다.
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    if (request.getRequestURI().equals("/api/v1/users/signup")) {

      String signUpEmail = emailMasking(AuditorContext.getCurrentEmail());
      AuditorContext.clear();

      return Optional.of(signUpEmail);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    // 로그인한 사용자의 ID Masking 처리
    return Optional.of(emailMasking(authentication.getName()));
  }

  // TODO 추후에 암, 복호화 하는 것도 추가해볼만하다.
  private String emailMasking(String email) {

    StringBuilder sb = new StringBuilder(email);
    int halflength = sb.length() / 2;

    if (email.length() < 15) {
      sb.replace(halflength - 2, halflength + 2, "****");
    } else {
      sb.replace(halflength - 3, halflength + 2, "*****");
    }

    return sb.toString();
  }


}
