package com.locat.api.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class AbstractLocatSecurityFilter extends OncePerRequestFilter {

  /**
   * OAuth2 리다이렉트 URI <br>
   * Filter에서 제외되어야함
   */
  protected static final String OAUTH2_REDIRECT_PATH = "/v1/oauth2/redirect";

  protected void proceed(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    filterChain.doFilter(request, response);
  }

  protected void forward(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher(request.getServletPath()).forward(request, response);
  }

  protected boolean isOAuthRedirect(HttpServletRequest request) {
    return request.getRequestURI().startsWith(OAUTH2_REDIRECT_PATH);
  }
}
