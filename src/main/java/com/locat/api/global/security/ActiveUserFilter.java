package com.locat.api.global.security;

import static com.locat.api.global.security.SecurityConfig.PUBLIC_API_PATHS;

import com.locat.api.domain.user.entity.User;
import com.locat.api.global.auth.LocatUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class ActiveUserFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.isPublicApi(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    final User user = this.getCurrentAuthenticatedUser();
    if (!user.isActivated()) {
      throw new AccessDeniedException("Access Denied: User is not activated.");
    }

    filterChain.doFilter(request, response);
  }

  private User getCurrentAuthenticatedUser() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .map(LocatUserDetails.class::cast)
        .map(LocatUserDetails::getUser)
        .orElseThrow(
            () -> new AccessDeniedException("Access Denied: Unable to retrieve user details."));
  }

  private boolean isPublicApi(HttpServletRequest request) {
    return PUBLIC_API_PATHS.stream().anyMatch(request.getServletPath()::startsWith);
  }
}
