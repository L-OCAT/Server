package com.locat.api.global.security.filter;

import com.locat.api.global.annotation.AdminApi;
import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.web.HandlerMethodAnnotationResolver;
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
public class AdminApiAuthorizationFilter extends AbstractLocatSecurityFilter {

  private final HandlerMethodAnnotationResolver annotationResolver;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.shouldNotFilter(request)) {
      this.proceed(request, response, filterChain);
      return;
    }

    Optional<AdminApi> optionalAdminApi = this.annotationResolver.find(request, AdminApi.class);
    final boolean isNotAdminApi = optionalAdminApi.isEmpty();
    final boolean isAuthorizedRequest =
        isNotAdminApi || this.isAuthorizedAdmin(optionalAdminApi.get());

    if (isAuthorizedRequest) {
      this.proceed(request, response, filterChain);
      return;
    }

    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return this.isPublicApiAuthorized(request);
  }

  private boolean isAuthorizedAdmin(AdminApi adminApi) {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(authentication -> this.checkAdminPrivileges(authentication, adminApi))
        .orElse(false);
  }

  private boolean checkAdminPrivileges(Authentication authentication, AdminApi adminApi) {
    if (authentication.getPrincipal() instanceof LocatUserDetails userDetails) {
      return adminApi.superAdminOnly() ? userDetails.isSuperAdmin() : userDetails.isAdmin();
    }
    return false;
  }
}
