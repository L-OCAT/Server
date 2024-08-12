package com.locat.api.unit.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.locat.api.global.auth.LocatUserDetailsService;
import com.locat.api.global.auth.jwt.JwtProvider;
import com.locat.api.global.security.JwtAuthenticationFilter;
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

  @Mock private JwtProvider jwtProvider;

  @Mock private LocatUserDetailsService userDetailsService;

  @Mock private FilterChain filterChain;

  @Mock private Authentication authentication;

  @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
  }

  @Test
  @DisplayName("유효한 토큰이 주어지면 인증을 설정한다.")
  void givenValidToken_whenDoFilterInternal_thenSetAuthentication()
      throws ServletException, IOException {
    // Given
    String token = "validToken";
    String username = "user";
    Claims claims = mock(Claims.class);

    when(jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(token);
    when(jwtProvider.parse(token)).thenReturn(claims);
    when(claims.getSubject()).thenReturn(username);
    when(userDetailsService.createAuthentication(username)).thenReturn(authentication);

    // When
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // Then
    SecurityContext context = SecurityContextHolder.getContext();
    assertThat(context.getAuthentication()).isEqualTo(authentication);
    verify(jwtProvider).resolve(any(HttpServletRequest.class));
    verify(jwtProvider).parse(token);
    verify(userDetailsService).createAuthentication(username);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("유효하지 않은 토큰이 주어지면 인증을 설정하지 않는다.")
  void givenInvalidToken_whenDoFilterInternal_thenSendUnauthorized()
      throws ServletException, IOException {
    // Given
    String token = "invalidToken";

    when(jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(token);
    when(jwtProvider.parse(token)).thenThrow(new JwtException("Invalid token"));

    // When
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // Then
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    verify(jwtProvider).resolve(any(HttpServletRequest.class));
    verify(jwtProvider).parse(token);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("토큰이 주어지지 않으면 인증을 설정하지 않는다.")
  void givenNoToken_whenDoFilterInternal_thenDoNotSetAuthentication()
      throws ServletException, IOException {
    // Given
    when(jwtProvider.resolve(any(HttpServletRequest.class))).thenReturn(null);

    // When
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // Then
    SecurityContext context = SecurityContextHolder.getContext();
    assertThat(context.getAuthentication()).isNull();
    verify(jwtProvider).resolve(any(HttpServletRequest.class));
    verify(filterChain).doFilter(request, response);
  }
}
