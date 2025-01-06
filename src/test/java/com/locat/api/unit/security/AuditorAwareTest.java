package com.locat.api.unit.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.locat.api.global.security.handler.LocatAuditorAware;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class AuditorAwareTest {

  private static final String TEST_USER_NAME = "test-user";

  @InjectMocks private LocatAuditorAware auditorAware;
  @Mock private SecurityContext securityContext;
  @Mock private Authentication authentication;
  @Mock private LocatUserDetails locatUserDetails;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(this.securityContext);
  }

  @Test
  @DisplayName("인증된 사용자라면, 현재 사용자의 ID / 이름을 반환해야 한다.")
  void whenAuthenticatedShouldReturnUserId() {
    // Given
    Long userId = 123L;
    given(this.securityContext.getAuthentication()).willReturn(this.authentication);
    given(this.authentication.isAuthenticated()).willReturn(true);
    given(this.authentication.getPrincipal()).willReturn(this.locatUserDetails);
    given(this.authentication.getName()).willReturn(TEST_USER_NAME);
    given(this.locatUserDetails.getId()).willReturn(userId);

    // When
    Optional<Long> currentAuditor = this.auditorAware.getCurrentAuditor();
    Optional<String> currentAuditorName = this.auditorAware.getCurrentAuditorName();

    // Then
    assertThat(currentAuditor).isPresent().hasValue(userId);
    assertThat(currentAuditorName).isPresent().hasValue(TEST_USER_NAME);
  }

  @Test
  @DisplayName("인증되지 않은 사용자[익명 사용자]라면, Optional.empty()를 반환해야 한다.")
  void whenNotAuthenticatedShouldReturnEmpty() {
    // Given
    given(this.securityContext.getAuthentication()).willReturn(this.authentication);
    given(this.authentication.isAuthenticated()).willReturn(false);

    // When
    Optional<Long> currentAuditor = this.auditorAware.getCurrentAuditor();
    Optional<String> currentAuditorName = this.auditorAware.getCurrentAuditorName();

    // Then
    assertThat(currentAuditor).isEmpty();
    assertThat(currentAuditorName).isEmpty();
  }

  @Test
  @DisplayName("인증 정보가 없는 경우, Optional.empty()를 반환해야 한다.")
  void whenNoAuthenticationShouldReturnEmpty() {
    // Given
    given(this.securityContext.getAuthentication()).willReturn(null);

    // When
    Optional<Long> currentAuditor = this.auditorAware.getCurrentAuditor();
    Optional<String> currentAuditorName = this.auditorAware.getCurrentAuditorName();

    // Then
    assertThat(currentAuditor).isEmpty();
    assertThat(currentAuditorName).isEmpty();
  }
}
