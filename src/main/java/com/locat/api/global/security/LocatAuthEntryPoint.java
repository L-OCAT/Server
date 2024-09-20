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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class LocatAuthEntryPoint implements AuthenticationEntryPoint {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("UnAuthorized: {}", authException.getMessage());
    }

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    try (PrintWriter out = response.getWriter()) {
      out.print(MAPPER.writeValueAsString(ErrorResponse.unauthorized()));
      out.flush();
    }
  }
}
