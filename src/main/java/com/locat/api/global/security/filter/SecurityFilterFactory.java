package com.locat.api.global.security.filter;

import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.security.LocatAuditorAware;
import com.locat.api.global.security.SecurityProperties;
import com.locat.api.global.web.HandlerMethodAnnotationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterFactory {

  private final SecurityProperties securityProperties;
  private final HandlerMethodAnnotationResolver annotationResolver;
  private final LocatAuditorAware auditorAware;
  private final ApplicationEventPublisher eventPublisher;
  private final LocatUserDetailsService userDetailsService;
  private final JwtProvider jwtProvider;

  public PublicApiAccessControlFilter publicAccess() {
    return new PublicApiAccessControlFilter(this.securityProperties, this.annotationResolver);
  }

  public JwtAuthenticationFilter jwtAuth() {
    return new JwtAuthenticationFilter(this.jwtProvider, this.userDetailsService);
  }

  public AdminApiAuthorizationFilter adminAuth() {
    return new AdminApiAuthorizationFilter(
        this.annotationResolver, this.auditorAware, this.eventPublisher);
  }
}
