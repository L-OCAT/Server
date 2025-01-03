package com.locat.api.unit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.enums.StatusType;
import com.locat.api.domain.user.enums.UserType;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.security.userdetails.LocatUserDetails;
import com.locat.api.global.security.userdetails.impl.LocatUserDetailsImpl;
import com.locat.api.global.security.userdetails.impl.LocatUserDetailsServiceImpl;
import com.locat.api.global.utils.HashingUtils;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

final class LocatUserDetailsServiceTest {

  private static final User MOCK_USER =
      User.builder()
          .id(1L)
          .nickname("LOCAT1523")
          .email("test@locat.kr")
          .emailHash(HashingUtils.hash("test@locat.kr"))
          .password("enCryptedPassword")
          .statusType(StatusType.ACTIVE)
          .userType(UserType.SUPER_ADMIN)
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .deletedAt(null)
          .build();
  private static final LocatUserDetails MOCK_USER_DETAILS = LocatUserDetailsImpl.from(MOCK_USER);

  @InjectMocks private LocatUserDetailsServiceImpl service;
  @Mock private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("username을 가지는 활성 사용자가 있으면 UserDetails를 반환한다.")
  void testLoadUserByUsername() {
    // Given
    given(this.userService.findByEmail(MOCK_USER.getEmail())).willReturn(Optional.of(MOCK_USER));

    // When
    UserDetails userDetails = this.service.loadUserByUsername(MOCK_USER.getEmail());

    // Then
    assertThat(userDetails).isNotNull();
  }

  @Test
  @DisplayName("username을 가지는 활성 사용자가 없으면 NoSuchEntityException을 던진다.")
  void testLoadUserByUsernameWithNoSuchEntityException() {
    // Given
    given(userService.findByEmail(MOCK_USER.getEmail())).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.loadUserByUsername(MOCK_USER.getEmail()))
        .isExactlyInstanceOf(NoSuchEntityException.class);
  }

  @Test
  @DisplayName("UserDetails로부터 Authentication을 생성한다.")
  void testCreateAuthentication() {
    // When
    Authentication authentication = this.service.createAuthentication(MOCK_USER_DETAILS);

    // Then
    assertThat(authentication).isNotNull();
  }

  @Test
  @DisplayName("Claims로부터 Authentication을 생성한다.")
  void testCreateAuthenticationWithClaims() {
    // Given
    Claims mockClaims = mock(Claims.class);
    given(mockClaims.getSubject()).willReturn(MOCK_USER.getEmail());
    given(userService.findByEmail(MOCK_USER.getEmail())).willReturn(Optional.of(MOCK_USER));

    // When
    Authentication authentication = this.service.createAuthentication(mockClaims);

    // Then
    assertThat(authentication).isNotNull();
  }

  @Test
  @DisplayName("Authentication으로부터 권한 정보를 추출한다.")
  void testExtractAuthority() {
    // Given
    Authentication mockAuthentication =
        new UsernamePasswordAuthenticationToken(
            MOCK_USER_DETAILS, null, Set.of((GrantedAuthority) () -> "SUPER_ADMIN"));

    // When
    String authority = this.service.extractAuthority(mockAuthentication);

    // Then
    assertThat(authority).isEqualTo("SUPER_ADMIN");
  }
}
