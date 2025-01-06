package com.locat.api.global.security.filter.impl;

import static com.locat.api.global.security.handler.LocatAuditorAware.PRINCIPAL_ANONYMOUS_USER;

import com.locat.api.global.event.AdminAuditEvent;
import com.locat.api.global.security.annotation.AdminApi;
import com.locat.api.global.security.filter.AbstractLocatSecurityFilter;
import com.locat.api.global.security.handler.LocatAuditorAware;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.web.resolver.HandlerMethodAnnotationResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class AdminApiAuthorizationFilter extends AbstractLocatSecurityFilter {

  private final HandlerMethodAnnotationResolver annotationResolver;
  private final LocatAuditorAware auditorAware;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.shouldNotFilter(request)) {
      this.proceed(request, response, filterChain);
      return;
    }

    Optional<AdminApi> optionalAdminApi = this.annotationResolver.find(request, AdminApi.class);
    final boolean isAdminApi = optionalAdminApi.isPresent();
    final boolean isAuthorizedRequest =
        !isAdminApi || this.isAuthorizedAdmin(optionalAdminApi.get());

    if (!isAuthorizedRequest) {
      this.handleUnauthorizedRequest(request, response, optionalAdminApi.get());
      return;
    }

    try {
      this.proceed(request, response, filterChain);
    } finally {
      if (isAdminApi && optionalAdminApi.get().audit()) {
        this.publishAuditEvent(request, response, optionalAdminApi.get(), true);
      }
    }
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

  private void handleUnauthorizedRequest(
      HttpServletRequest request, HttpServletResponse response, AdminApi adminApi)
      throws IOException {
    if (adminApi.audit()) {
      this.publishAuditEvent(request, response, adminApi, false);
    }
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }

  private void publishAuditEvent(
      HttpServletRequest request,
      HttpServletResponse response,
      AdminApi adminApi,
      boolean isSuccessful) {
    String username = this.auditorAware.getCurrentAuditorName().orElse(PRINCIPAL_ANONYMOUS_USER);
    AdminAuditEvent auditEvent =
        AdminAuditEvent.builder()
            .email(username)
            .superAdminOnly(adminApi.superAdminOnly())
            .method(request.getMethod())
            .requestUri(request.getRequestURI())
            .httpStatus(response.getStatus())
            .isSuccessful(isSuccessful)
            .remoteAddress(request.getRemoteAddr())
            .userAgent(request.getHeader(HttpHeaders.USER_AGENT))
            .timestamp(LocalDateTime.now())
            .build();
    this.eventPublisher.publishEvent(auditEvent);
  }
}
