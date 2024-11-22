package com.locat.api.unit.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.impl.UserValidationServiceImpl;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.infra.persistence.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserValidationServiceTest {

  @InjectMocks private UserValidationServiceImpl service;
  @Mock private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("OAuth ID 중복 여부 검증 테스트")
  @ParameterizedTest
  @CsvSource({"597612893, false", "117782_a11, true"})
  void testIsExists(String oauthId, boolean expected) {
    // Given
    given(this.userRepository.existsByOauthId(oauthId)).willReturn(expected);

    // When
    boolean result = this.service.isExists(oauthId, UserInfoValidationType.OAUTH_ID);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("중복 또는 금지되지 않은 닉네임 값이 주어지면, 예외가 발생하지 않아야 한다.")
  void testValidateNickname() {
    // Given
    String nickname = "TestUser";
    given(this.userRepository.existsByNickname(nickname)).willReturn(false);

    // When & Then
    assertThatCode(() -> this.service.validateNickname(nickname)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("주어진 닉네임이 이미 사용 중인 경우, 예외를 던져야 한다.")
  void testValidateNicknameWithDuplicatedValue() {
    // Given
    String nickname = "TestUser";
    given(this.userRepository.existsByNickname(nickname)).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> this.service.validateNickname(nickname))
        .isExactlyInstanceOf(DuplicatedException.class);
  }

  @DisplayName("닉네임 정책 테스트")
  @ParameterizedTest(name = "입력값 - {0}")
  @ValueSource(strings = {"TestUser!", "사용@자", "admin", "운영자"})
  void testValidateNicknameWithInvalidValue(String nickname) {
    // Given
    given(this.userRepository.existsByNickname(nickname)).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> this.service.validateNickname(nickname))
        .isExactlyInstanceOf(InvalidParameterException.class);
  }

  @Test
  @DisplayName("중복 또는 금지되지 않은 이메일 값이 주어지면, 예외가 발생하지 않아야 한다.")
  void testValidateEmail() {
    // Given
    String email = "test@locat.kr";
    given(this.userRepository.existsByEmailHash(email)).willReturn(false);

    // When & Then
    assertThatCode(() -> this.service.validateEmail(email)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("주어진 이메일이 이미 사용 중인 경우, 예외를 던져야 한다.")
  void testValidateEmailWithDuplicatedValue() {
    // Given
    String email = "test@locat.kr";
    given(this.userRepository.existsByEmailHash(HashingUtils.hash(email))).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> this.service.validateEmail(email))
        .isExactlyInstanceOf(DuplicatedException.class);
  }

  @DisplayName("이메일 정책 테스트")
  @ParameterizedTest(name = "입력값 - {0}")
  @ValueSource(strings = {"test@locat", "test@locat.", "test@locat.12"})
  void testValidateEmailWithInvalidFormat(String email) {
    // Given
    given(this.userRepository.existsByEmailHash(email)).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> this.service.validateEmail(email))
        .isExactlyInstanceOf(InvalidParameterException.class);
  }
}
