package com.locat.api.unit.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.locat.api.global.auth.LocatUserDetails;
import com.locat.api.global.security.LocatAuditorAware;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class AuditorAwareTest {

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;

  @Mock private LocatUserDetails locatUserDetails;

  @InjectMocks private LocatAuditorAware locatAuditorAware;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  @DisplayName("인증된 사용자라면, 현재 사용자의 ID를 반환해야 한다.")
  void whenAuthenticatedShouldReturnUserId() {
    // Given
    Long userId = 123L;
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(locatUserDetails);
    when(locatUserDetails.getId()).thenReturn(userId);

    // When
    Optional<Long> currentAuditor = locatAuditorAware.getCurrentAuditor();

    // Then
    assertTrue(currentAuditor.isPresent());
    assertEquals(userId, currentAuditor.get());
    assertAll(
        () -> verify(securityContext, times(1)).getAuthentication(),
        () -> verify(authentication, times(1)).isAuthenticated(),
        () -> verify(authentication, times(1)).getPrincipal(),
        () -> verify(locatUserDetails, times(1)).getId());
  }

  @Test
  @DisplayName("인증되지 않은 사용자[익명 사용자]라면, Optional.empty()를 반환해야 한다.")
  void whenNotAuthenticatedShouldReturnEmpty() {
    // Given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(false);

    // When
    Optional<Long> currentAuditor = locatAuditorAware.getCurrentAuditor();

    // Then
    assertTrue(currentAuditor.isEmpty());
    assertAll(
        () -> verify(securityContext, times(1)).getAuthentication(),
        () -> verify(authentication, times(1)).isAuthenticated());
  }

  @Test
  @DisplayName("인증 정보가 없는 경우, Optional.empty()를 반환해야 한다.")
  void whenNoAuthenticationShouldReturnEmpty() {
    // Given
    when(securityContext.getAuthentication()).thenReturn(null);

    // When
    Optional<Long> currentAuditor = locatAuditorAware.getCurrentAuditor();

    // Then
    assertTrue(currentAuditor.isEmpty());
    assertAll(() -> verify(securityContext, times(1)).getAuthentication());
  }
}
