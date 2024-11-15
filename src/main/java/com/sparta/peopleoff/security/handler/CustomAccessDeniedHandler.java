package com.sparta.peopleoff.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final String ROLE_PREFIX = "ROLE_";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException ex)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String authorities = "";
    if (auth != null) {
      authorities = auth.getAuthorities().stream()
          .map(grantedAuthority -> grantedAuthority.getAuthority())
          .collect(Collectors.joining(", "))
          .replace(ROLE_PREFIX, "");
    }

    ApiResponse<Object> commonResponse = ApiResponse.ERROR(ResErrorCode.ACCESS_DENIED,
        "권한이 없습니다. 현재 사용자 권한: " + authorities);

    new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);
  }
}
