package com.locat.api.unit.security.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.locat.api.global.event.AdminAuditEvent;
import com.locat.api.global.security.annotation.AdminApi;
import com.locat.api.global.security.filter.impl.AdminApiAuthorizationFilter;
import com.locat.api.global.security.handler.LocatAuditorAware;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.web.resolver.HandlerMethodAnnotationResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

final class AdminApiAuthorizationFilterTest {

  @InjectMocks private AdminApiAuthorizationFilter filter;
  @Mock private HandlerMethodAnnotationResolver annotationResolver;
  @Mock private LocatAuditorAware auditorAware;
  @Mock private ApplicationEventPublisher eventPublisher;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Admin API가 아닌 요청이면, 필터 체인을 그대로 진행한다.")
  void proceedFilterChainWhenNotAdminApi() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    given(this.annotationResolver.find(request, AdminApi.class)).willReturn(Optional.empty());

    // When
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // Then
    then(filterChain).should().doFilter(request, response);
  }

  @Test
  @DisplayName("Admin API 접근 권한이 없으면, FORBIDDEN 응답을 반환한다.")
  void returnForbiddenIfUnauthorized() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    AdminApi adminApi = mock(AdminApi.class);
    try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder =
        mockStatic(SecurityContextHolder.class)) {

      given(this.annotationResolver.find(request, AdminApi.class))
          .willReturn(Optional.of(adminApi));
      given(adminApi.audit()).willReturn(true);
      mockedSecurityContextHolder
          .when(SecurityContextHolder::getContext)
          .thenReturn(mock(SecurityContext.class));

      // When
      ReflectionTestUtils.invokeMethod(
          this.filter, "doFilterInternal", request, response, filterChain);
    }

    // Then
    then(response).should().sendError(HttpServletResponse.SC_FORBIDDEN);
    then(this.eventPublisher).should().publishEvent(any(AdminAuditEvent.class));
    then(filterChain).should(never()).doFilter(request, response);
  }

  @Test
  @DisplayName("SuperAdmin 전용 API에서 일반 Admin 권한이면, FORBIDDEN 응답을 반환한다.")
  void returnForbiddenIfNotSuperAdminForSuperAdminApi() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    AdminApi adminApi = mock(AdminApi.class);
    Authentication authentication = mock(Authentication.class);
    LocatUserDetails userDetails = mock(LocatUserDetails.class);

    given(this.annotationResolver.find(request, AdminApi.class)).willReturn(Optional.of(adminApi));
    given(adminApi.superAdminOnly()).willReturn(true);
    given(adminApi.audit()).willReturn(false);
    given(authentication.isAuthenticated()).willReturn(true);
    given(authentication.getPrincipal()).willReturn(userDetails);
    given(userDetails.isSuperAdmin()).willReturn(false);

    SecurityContext context = mock(SecurityContext.class);
    given(context.getAuthentication()).willReturn(authentication);
    SecurityContextHolder.setContext(context);

    // When
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // Then
    then(response).should().sendError(HttpServletResponse.SC_FORBIDDEN);
    then(this.eventPublisher).should(never()).publishEvent(any(AdminAuditEvent.class));
    then(filterChain).should(never()).doFilter(request, response);
  }

  @Test
  @DisplayName("Admin API 접근 권한이 있는 요청이면, 필터 체인을 진행한다.")
  void proceedFilterChainIfAuthorized() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    AdminApi adminApi = mock(AdminApi.class);
    Authentication authentication = mock(Authentication.class);
    LocatUserDetails userDetails = mock(LocatUserDetails.class);

    given(this.annotationResolver.find(request, AdminApi.class)).willReturn(Optional.of(adminApi));
    given(adminApi.superAdminOnly()).willReturn(false);
    given(authentication.isAuthenticated()).willReturn(true);
    given(authentication.getPrincipal()).willReturn(userDetails);
    given(userDetails.isAdmin()).willReturn(true);

    SecurityContext context = mock(SecurityContext.class);
    given(context.getAuthentication()).willReturn(authentication);
    SecurityContextHolder.setContext(context);

    // When
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // Then
    then(filterChain).should().doFilter(request, response);
    then(eventPublisher).should(never()).publishEvent(any(AdminAuditEvent.class));
  }

  @Test
  @DisplayName("Admin API 요청이 성공적으로 처리된 경우, 감사 이벤트를 발행한다.")
  void publishAuditEventOnSuccessfulAdminApiRequest() throws Exception {
    // Given
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    AdminApi adminApi = mock(AdminApi.class);
    Authentication authentication = mock(Authentication.class);
    LocatUserDetails userDetails = mock(LocatUserDetails.class);

    given(this.annotationResolver.find(request, AdminApi.class)).willReturn(Optional.of(adminApi));
    given(adminApi.superAdminOnly()).willReturn(false);
    given(adminApi.audit()).willReturn(true);
    given(authentication.isAuthenticated()).willReturn(true);
    given(authentication.getPrincipal()).willReturn(userDetails);
    given(userDetails.isAdmin()).willReturn(true);

    SecurityContext context = mock(SecurityContext.class);
    given(context.getAuthentication()).willReturn(authentication);
    SecurityContextHolder.setContext(context);

    // When
    ReflectionTestUtils.invokeMethod(
        this.filter, "doFilterInternal", request, response, filterChain);

    // Then
    then(filterChain).should().doFilter(request, response);
    then(this.eventPublisher).should().publishEvent(any(AdminAuditEvent.class));
  }
}
