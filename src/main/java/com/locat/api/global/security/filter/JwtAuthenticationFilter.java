package com.locat.api.global.security.filter;

import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractLocatSecurityFilter {

  private final JwtProvider jwtProvider;
  private final LocatUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.shouldNotFilter(request)) {
      this.proceed(request, response, filterChain);
      return;
    }

    Optional<String> userToken = Optional.of(request).map(this.jwtProvider::resolve);

    if (userToken.isPresent()) {
      this.processAuthentication(userToken.get(), response);
      filterChain.doFilter(request, response);
      return;
    }

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No Authorization Header");
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return this.isPublicApiAuthorized(request);
  }

  private void processAuthentication(String token, HttpServletResponse response)
      throws IOException {
    try {
      Claims claims = this.jwtProvider.parse(token);
      this.setAuthentication(claims);
    } catch (JwtException ex) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
  }

  private void setAuthentication(Claims claims) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = this.userDetailsService.createAuthentication(claims);
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }
}
