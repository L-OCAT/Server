package com.locat.api.unit.admin;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.locat.api.domain.admin.service.impl.AdminInternalServiceImpl;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

final class AdminInternalServiceTest {

  private static final String TEST_EMAIL = "test@locat.kr";

  @InjectMocks private AdminInternalServiceImpl service;
  @Mock private UserService userService;
  @Mock private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("사용자가 존재하고, 비밀번호가 만료된 경우 비밀번호를 재설정한다.")
  void shouldResetPassword() {
    // Given
    final String newPassword = "newPassword";
    given(this.userService.findByEmail(TEST_EMAIL))
        .willReturn(Optional.of(User.builder().id(1L).isPasswordExpired(true).build()));

    // When & Then
    assertThatCode(() -> this.service.resetPassword(TEST_EMAIL, newPassword))
        .doesNotThrowAnyException();
    then(this.userService).should().findByEmail(TEST_EMAIL);
    then(this.passwordEncoder).should().encode(newPassword);
  }

  @Test
  @DisplayName("사용자가 존재하지 않는 경우 NoSuchEntityException 예외를 발생시킨다.")
  void shouldThrowNoSuchEntityExceptionWhenUserNotFound() {
    // Given
    given(this.userService.findByEmail(TEST_EMAIL)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.resetPassword(TEST_EMAIL, "newPassword"))
        .isExactlyInstanceOf(NoSuchEntityException.class);
    then(this.userService).should().findByEmail(TEST_EMAIL);
  }

  @Test
  @DisplayName("사용자가 존재하지만 비밀번호가 만료되지 않은 경우 NoSuchEntityException 예외를 발생시킨다.")
  void shouldThrowNoSuchEntityExceptionWhenPasswordNotExpired() {
    // Given
    given(this.userService.findByEmail(TEST_EMAIL))
        .willReturn(Optional.of(User.builder().id(1L).isPasswordExpired(false).build()));

    // When & Then
    assertThatThrownBy(() -> this.service.resetPassword(TEST_EMAIL, "newPassword"))
        .isExactlyInstanceOf(NoSuchEntityException.class);
    then(this.userService).should().findByEmail(TEST_EMAIL);
  }

  @Test
  @DisplayName("사용자가 존재하면, 사용자의 유저 타입을 변경한다.")
  void shouldUpdateUserType() {
    // Given
    final int level = 1;
    given(this.userService.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));

    // When & Then
    assertThatCode(() -> this.service.updateUserType(1L, level)).doesNotThrowAnyException();
    then(this.userService).should().findById(1L);
  }

  @Test
  @DisplayName("사용자가 존재하지 않는 경우 NoSuchEntityException 예외를 발생시킨다.")
  void shouldThrowNoSuchEntityExceptionWhenUserNotFoundForUpdateUserType() {
    // Given
    given(this.userService.findById(1L)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.updateUserType(1L, 1))
        .isExactlyInstanceOf(NoSuchEntityException.class);
    then(this.userService).should().findById(1L);
  }

  @Test
  @DisplayName("존재하지 않는 Level로 유저 타입을 변경하려고 시도하면 IllegalArgumentException 예외를 발생시킨다.")
  void shouldThrowIllegalArgumentExceptionWhenInvalidLevel() {
    // Given
    given(this.userService.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));

    // When & Then
    assertThatThrownBy(() -> this.service.updateUserType(1L, 100))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    then(this.userService).should().findById(1L);
  }
}
