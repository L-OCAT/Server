package com.locat.api.global.security.filter;

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

public class ActiveUserFilter extends AbstractLocatSecurityFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (super.isPublicApi(request) || super.isOAuthRedirect(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    this.getCurrentAuthenticatedUser().assertActivated();
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
}
