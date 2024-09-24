package com.locat.api.global.security.filter;

import com.locat.api.global.exception.NoApiKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PublicApiKeyFilter extends AbstractLocatSecurityFilter {

  private final String publicApiKey;

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (super.isPublicApi(request)) {
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
    if (!this.publicApiKey.equals(requestedApiKey)) {
      throw new NoApiKeyException("Access Denied: Invalid API Key.");
    }
  }
}
