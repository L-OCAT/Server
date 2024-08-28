package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.locat.api.global.utils.TypeCaster;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeCasterTest {

  @Test
  @DisplayName("String → Integer 변환에 성공해야 한다.")
  void castStringToInteger() {
    // Given
    String value = "123";

    // When
    Integer result = TypeCaster.cast(value, Integer.class);

    // Then
    assertThat(result).isEqualTo(123);
    assertThat(result).isExactlyInstanceOf(Integer.class);
  }

  @Test
  @DisplayName("String → Double 변환에 성공해야 한다.")
  void castStringToDouble() {
    // Given
    String value = "123.45";

    // When
    Double result = TypeCaster.cast(value, Double.class);

    // Then
    assertThat(result).isEqualTo(123.45);
    assertThat(result).isExactlyInstanceOf(Double.class);
  }

  @Test
  @DisplayName("String → Boolean 변환에 성공해야 한다.")
  void castStringToBoolean() {
    // Given
    String value = "true";

    // When
    Boolean result = TypeCaster.cast(value, Boolean.class);

    // Then
    assertThat(result).isTrue();
    assertThat(result).isExactlyInstanceOf(Boolean.class);
  }

  @Test
  @DisplayName("지원하지 않는 타입으로 변환하려고 하면, 예외가 발생해야 한다.")
  void testError() {
    // Given
    String value = "123";

    // When * Then
    assertThatThrownBy(() -> TypeCaster.cast(value, Object.class))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }
}
