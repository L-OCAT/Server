package com.locat.api.unit.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.security.filter.ActiveUserFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class ActiveEndUserFilterTest {

  @InjectMocks private ActiveUserFilter filter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Public API 또는 OAuth 리다이렉트 요청인 경우, 필터를 통과해야 한다.")
  void testWhenPublicApiOrOAuthRedirect() throws ServletException, IOException {
    // Given
    String[] apiPaths = {
      "/v1/oauth2/redirect/kakao",
      "/v1/oauth2/redirect/apple",
      "/v1/auth/token",
      "/v1/auth/email-verification"
    };

    // When & Then
    for (String path : apiPaths) {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      FilterChain filterChain = mock(FilterChain.class);
      request.setRequestURI(path);
      assertThatCode(() -> this.filter.doFilter(request, response, filterChain))
          .doesNotThrowAnyException();
      verify(filterChain, times(1)).doFilter(request, response);
    }
  }

  @Test
  @DisplayName("ACTIVE 상태가 아닌 사용자의 요청의 경우, 예외를 던져야 한다.")
  void testWhenUserNotActivated() {
    // Given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = new MockFilterChain();

    // When
    EndUser mockUser = mock(EndUser.class);
    LocatUserDetails mockUserDetails = mock(LocatUserDetails.class);
    when(mockUserDetails.getUser()).thenReturn(mockUser);
    doThrow(new AccessDeniedException("Access Denied: User is not activated."))
        .when(mockUser)
        .assertActivated();
    // MockUserDetails로 테스트 토큰을 생성해 현재 SecurityContext에 설정
    TestingAuthenticationToken authenticationToken =
        new TestingAuthenticationToken(mockUserDetails, null);
    authenticationToken.setAuthenticated(true);

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authenticationToken);
    SecurityContextHolder.setContext(securityContext);

    // Then
    assertThatThrownBy(() -> this.filter.doFilter(request, response, filterChain))
        .isExactlyInstanceOf(AccessDeniedException.class)
        .hasMessage("Access Denied: User is not activated.");
  }
}
