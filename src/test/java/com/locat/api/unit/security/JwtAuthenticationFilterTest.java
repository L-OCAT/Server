package com.locat.api.unit.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.security.filter.JwtAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

  @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Mock private JwtProvider jwtProvider;
  @Mock private LocatUserDetailsService userDetailsService;
  @Mock private FilterChain filterChain;
  @Mock private Authentication authentication;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("유효한 토큰이 주어지면 인증을 설정한다.")
  void testWhenValidAuthorizationHeader() throws ServletException, IOException {
    // Given
    String token = "validToken";
    String username = "user";
    Claims claims = mock(Claims.class);

    when(this.jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(token);
    when(this.jwtProvider.parse(token)).thenReturn(claims);
    when(claims.getSubject()).thenReturn(username);
    when(this.userDetailsService.createAuthentication(username)).thenReturn(this.authentication);

    // When
    this.jwtAuthenticationFilter.doFilter(this.request, this.response, this.filterChain);

    // Then
    SecurityContext context = SecurityContextHolder.getContext();
    assertThat(context.getAuthentication()).isEqualTo(this.authentication);
    verify(this.jwtProvider).resolve(any(HttpServletRequest.class));
    verify(this.jwtProvider).parse(token);
    verify(this.userDetailsService).createAuthentication(username);
    verify(this.filterChain).doFilter(this.request, this.response);
  }

  @Test
  @DisplayName("유효하지 않은 토큰이 주어지면 인증을 설정하지 않는다.")
  void testWhenInvalidAuthorizationHeader() throws ServletException, IOException {
    // Given
    String token = "invalidToken";

    when(this.jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(token);
    when(this.jwtProvider.parse(token)).thenThrow(new JwtException("Invalid token"));

    // When
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // Then
    assertThat(this.response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    verify(this.jwtProvider).resolve(any(HttpServletRequest.class));
    verify(this.jwtProvider).parse(token);
    verify(this.filterChain).doFilter(this.request, this.response);
  }

  @Test
  @DisplayName("토큰이 주어지지 않으면 인증을 설정하지 않는다.")
  void testWhenNoAuthorizationHeader() throws ServletException, IOException {
    // Given
    when(this.jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(null);

    // When
    this.jwtAuthenticationFilter.doFilter(this.request, this.response, this.filterChain);

    // Then
    SecurityContext context = SecurityContextHolder.getContext();
    assertThat(context.getAuthentication()).isNull();
    verify(this.jwtProvider).resolve(any(HttpServletRequest.class));
    verify(this.filterChain).doFilter(this.request, this.response);
  }
}
