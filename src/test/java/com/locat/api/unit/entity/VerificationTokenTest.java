package com.locat.api.unit.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.auth.entity.VerificationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VerificationTokenTest {

  private static final String EMAIL = "user@example.com";
  private static final String CODE = "123456";
  private static final Long TIME_TO_LIVE = 300L;
  private VerificationCode verificationCode;

  @BeforeEach
  void setUp() {
    // Given
    this.verificationCode =
        VerificationCode.builder().email(EMAIL).code(CODE).timeToLive(TIME_TO_LIVE).build();
  }

  @Test
  @DisplayName("Builder로 VerificationCode 객체를 생성할 수 있다.")
  void testVerificationCodeBuilder() {
    // When & Then
    assertAll(
        () -> assertThat(this.verificationCode).isNotNull(),
        () -> assertThat(this.verificationCode.getEmail()).isEqualTo(EMAIL),
        () -> assertThat(this.verificationCode.getCode()).isEqualTo(CODE),
        () -> assertThat(this.verificationCode.getTimeToLive()).isEqualTo(TIME_TO_LIVE));
  }

  @Test
  @DisplayName("of() 메서드로 VerificationCode 객체를 생성할 수 있다.")
  void testOfMethod() {
    // Given
    String anotherEmail = "another@example.com";
    String anotherCode = "654321";
    Long anotherTimeToLive = 600L;

    // When
    VerificationCode newVerificationCode =
        VerificationCode.of(anotherEmail, anotherCode, anotherTimeToLive);

    // Then
    assertAll(
        () -> assertThat(newVerificationCode).isNotNull(),
        () -> assertThat(newVerificationCode.getEmail()).isEqualTo(anotherEmail),
        () -> assertThat(newVerificationCode.getCode()).isEqualTo(anotherCode),
        () -> assertThat(newVerificationCode.getTimeToLive()).isEqualTo(anotherTimeToLive));
  }
}
