package com.locat.api.unit.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.locat.api.global.security.LocatAccessDeniedHandler;
import com.locat.api.global.security.LocatAuthEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class SecurityExceptionHandlerTest {

  @Test
  @DisplayName("인증되지 않은 요청 시 401 상태 코드와 JSON 에러 메시지를 반환해야 한다")
  void testCommence() throws IOException {
    // Given
    LocatAuthEntryPoint authEntryPoint = new LocatAuthEntryPoint();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authException = mock(AuthenticationException.class);

    // When
    authEntryPoint.commence(request, response, authException);

    // Then
    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
  }

  @Test
  @DisplayName("인가되지 않은 요청 시 403 상태 코드와 JSON 에러 메시지를 반환해야 한다")
  void testHandle() throws IOException, ServletException {
    // Given
    LocatAccessDeniedHandler accessDeniedHandler = new LocatAccessDeniedHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AccessDeniedException accessDeniedException = mock(AccessDeniedException.class);

    // When
    accessDeniedHandler.handle(request, response, accessDeniedException);

    // Then
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
  }
}
