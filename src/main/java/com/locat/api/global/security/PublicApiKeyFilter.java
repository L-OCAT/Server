package com.locat.api.global.security;

import static com.locat.api.global.security.SecurityConfig.API_KEY_HEADER;
import static com.locat.api.global.security.SecurityConfig.PUBLIC_API_PATHS;

import com.locat.api.global.exception.NoApiKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

public class PublicApiKeyFilter extends OncePerRequestFilter {

  @Value("${service.api-key}")
  private String apiKey;

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.isPublicApi(request)) {
      Optional.of(request)
          .map(r -> r.getHeader(API_KEY_HEADER))
          .ifPresentOrElse(
              this::validateApiKey,
              () -> {
                throw new NoApiKeyException("Access Denied: API Key must be provided.");
              });
    }
    filterChain.doFilter(request, response);
  }

  private void validateApiKey(String requestedApiKey) {
    if (!this.apiKey.equals(requestedApiKey)) {
      throw new NoApiKeyException("Access Denied: Invalid API Key.");
    }
  }

  private boolean isPublicApi(HttpServletRequest request) {
    return PUBLIC_API_PATHS.stream().anyMatch(request.getRequestURI()::startsWith);
  }
}
