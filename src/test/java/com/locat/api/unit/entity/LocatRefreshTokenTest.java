package com.locat.api.unit.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.auth.entity.LocatRefreshToken;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocatRefreshTokenTest {

  private static final Long ID = 1L;
  private static final String EMAIL = "user@example.com";
  private static final String REFRESH_TOKEN = "sample_refresh_token";
  private static final Duration EXPIRATION_TIME = Duration.ofHours(1);

  private LocatRefreshToken locatRefreshToken;

  @BeforeEach
  void setUp() {
    // Given
    locatRefreshToken =
        LocatRefreshToken.builder()
            .id(ID)
            .email(EMAIL)
            .refreshToken(REFRESH_TOKEN)
            .refreshTokenExpiresIn(EXPIRATION_TIME.toSeconds())
            .build();
  }

  @Test
  @DisplayName("LocatRefreshToken Builder Test")
  void testLocatRefreshTokenBuilder() {
    // When & Then
    assertAll(
        () -> assertThat(locatRefreshToken).isNotNull(),
        () -> assertThat(locatRefreshToken.getId()).isEqualTo(ID),
        () -> assertThat(locatRefreshToken.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(locatRefreshToken.getRefreshToken()).isEqualTo(REFRESH_TOKEN),
        () ->
            assertThat(locatRefreshToken.getRefreshTokenExpiresIn())
                .isEqualTo(EXPIRATION_TIME.toSeconds()));
  }

  @Test
  @DisplayName("LocatRefreshToken from() Method Test")
  void testFromMethod() {
    // Given
    Duration expirationTime = Duration.ofHours(2);

    // When
    LocatRefreshToken newToken = LocatRefreshToken.from(ID, EMAIL, REFRESH_TOKEN, expirationTime);

    // Then
    assertAll(
        () -> assertThat(newToken).isNotNull(),
        () -> assertThat(newToken.getId()).isEqualTo(ID),
        () -> assertThat(newToken.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(newToken.getRefreshToken()).isEqualTo(REFRESH_TOKEN),
        () -> assertThat(newToken.getRefreshTokenExpiresIn()).isNotNull(),
        () ->
            assertThat(newToken.getRefreshTokenExpiresIn()).isEqualTo(expirationTime.toSeconds()));
  }

  @Test
  @DisplayName("LocatRefreshToken isNotMatched() Method Test")
  void testIsNotMatched() {
    // When & Then
    assertAll(
        () -> assertThat(locatRefreshToken.isNotMatched("wrong_token")).isTrue(),
        () -> assertThat(locatRefreshToken.isNotMatched(REFRESH_TOKEN)).isFalse());
  }
}
