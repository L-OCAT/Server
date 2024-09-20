package com.locat.api.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locat.api.domain.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class LocatAccessDeniedHandler implements AccessDeniedHandler {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("Access Denied: {}", accessDeniedException.getMessage());
    }

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    try (PrintWriter out = response.getWriter()) {
      out.print(MAPPER.writeValueAsString(ErrorResponse.forbidden()));
      out.flush();
    }
  }
}
