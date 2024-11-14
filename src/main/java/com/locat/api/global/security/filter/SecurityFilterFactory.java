package com.locat.api.global.security.filter;

import com.locat.api.global.security.common.ServiceProperties;
import com.locat.api.global.security.filter.impl.AdminApiAuthorizationFilter;
import com.locat.api.global.security.filter.impl.JwtAuthenticationFilter;
import com.locat.api.global.security.filter.impl.PublicApiAccessControlFilter;
import com.locat.api.global.security.handler.LocatAuditorAware;
import com.locat.api.global.security.jwt.JwtProvider;
import com.locat.api.global.security.userdetails.LocatUserDetailsService;
import com.locat.api.global.web.resolver.HandlerMethodAnnotationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterFactory {

  private final ServiceProperties serviceProperties;
  private final HandlerMethodAnnotationResolver annotationResolver;
  private final LocatAuditorAware auditorAware;
  private final ApplicationEventPublisher eventPublisher;
  private final LocatUserDetailsService userDetailsService;
  private final JwtProvider jwtProvider;

  public PublicApiAccessControlFilter publicAccess() {
    return new PublicApiAccessControlFilter(this.serviceProperties, this.annotationResolver);
  }

  public JwtAuthenticationFilter jwtAuth() {
    return new JwtAuthenticationFilter(this.jwtProvider, this.userDetailsService);
  }

  public AdminApiAuthorizationFilter adminAuth() {
    return new AdminApiAuthorizationFilter(
        this.annotationResolver, this.auditorAware, this.eventPublisher);
  }
}
