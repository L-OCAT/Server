package com.locat.api.unit.utils;

import static com.locat.api.global.utils.ValidationUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.locat.api.global.utils.ValidationUtils;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

  @Test
  @DisplayName("정규 표현식 - 유효한 입력값 테스트")
  void testRegexValidValues() {
    // Given
    String validNickName = "닉네임1";
    String validEmail = "locat@locat.kr";

    // When & Then
    assertThat(NICKNAME_PATTERN.matcher(validNickName).matches()).isTrue();
    assertThat(FORBIDDEN_NICKNAME_PATTERN.matcher(validNickName).matches()).isFalse();
    assertThat(EMAIL_PATTERN.matcher(validEmail).matches()).isTrue();
  }

  @Test
  @DisplayName("정규 표현식 - 유효하지 않은 입력값 테스트")
  void testRegexInvalidValues() {
    // Given
    String invalidNickName = "관리닉네임@@";
    String invalidEmail = "locat@locat";

    // When & Then
    assertThat(NICKNAME_PATTERN.matcher(invalidNickName).matches()).isFalse();
    assertThat(FORBIDDEN_NICKNAME_PATTERN.matcher(invalidNickName).matches()).isTrue();
    assertThat(EMAIL_PATTERN.matcher(invalidEmail).matches()).isFalse();
  }

  @Test
  @DisplayName("조건식이 true로 평가되면, 예외를 던져야 한다.")
  void testThrowIfTrue() {
    // Given
    String value = "value";

    // When & Then
    assertThatThrownBy(
            () -> ValidationUtils.throwIf(value, v -> !v.isEmpty(), RuntimeException::new))
        .isExactlyInstanceOf(RuntimeException.class);
    assertThatThrownBy(
            () -> ValidationUtils.throwIf(value, v -> v.equals("value"), RuntimeException::new))
        .isExactlyInstanceOf(RuntimeException.class);
    assertThatThrownBy(
            () ->
                ValidationUtils.throwIfAny(
                    value,
                    List.of(String::isEmpty, v -> v.equals("value")),
                    List.of(RuntimeException::new, RuntimeException::new)))
        .isExactlyInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("value가 null이거나, 조건식이 false로 평가되면, 예외를 던지지 않아야 한다.")
  void testThrowIfFalseOrNull() {
    // Given
    String value1 = "value";
    String value2 = null;

    // When & Then
    assertThatCode(() -> ValidationUtils.throwIf(value1, String::isEmpty, RuntimeException::new))
        .doesNotThrowAnyException();
    assertThatCode(
            () ->
                ValidationUtils.throwIf(
                    value1, v -> v.equals("notEqualValue"), RuntimeException::new))
        .doesNotThrowAnyException();
    assertThatCode(() -> ValidationUtils.throwIf(value2, String::isEmpty, RuntimeException::new))
        .doesNotThrowAnyException();
    assertThatCode(
            () ->
                ValidationUtils.throwIfAny(
                    value1,
                    List.of(String::isEmpty, v -> v.equals("notEqualValue")),
                    List.of(RuntimeException::new, RuntimeException::new)))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("검사 조건과 exceptionSupplier의 수가 일치하지 않으면, 예외를 던져야 한다.")
  void testThrowIfAny() {
    // Given
    String value = "value";

    // When & Then
    assertThatThrownBy(
            () ->
                ValidationUtils.throwIfAny(
                    value,
                    List.of(String::isEmpty, v -> v.equals("testValue")), // size: 2
                    List.of(RuntimeException::new))) // size: 1
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }
}
