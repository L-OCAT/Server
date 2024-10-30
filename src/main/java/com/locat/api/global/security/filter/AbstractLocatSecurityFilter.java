package com.locat.api.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class AbstractLocatSecurityFilter extends OncePerRequestFilter {

  protected static final String PUBLIC_API_AUTHORIZED = "SECURITY_PUBLIC_API_AUTHORIZED";

  protected void proceed(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    filterChain.doFilter(request, response);
  }

  protected boolean isPublicApiAuthorized(HttpServletRequest request) {
    return Boolean.TRUE.equals(request.getAttribute(PUBLIC_API_AUTHORIZED));
  }
}
