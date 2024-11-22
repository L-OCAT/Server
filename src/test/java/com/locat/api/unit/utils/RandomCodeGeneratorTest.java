package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.global.utils.RandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomCodeGeneratorTest {

  private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  @Test
  @DisplayName("주어진 길이의 랜덤 코드를 생성한다.")
  void testGenerateCode() {
    // Given
    final int requiredLength = 6;

    // When
    final String generatedCode = RandomGenerator.nextCode(requiredLength);

    // Then
    assertThat(generatedCode).hasSize(requiredLength);
    assertThat(generatedCode.chars().allMatch(c -> CHARACTERS_FOR_CODE.indexOf(c) >= 0)).isTrue();
  }

  @Test
  @DisplayName("주어진 길이의 랜덤 바이트 배열을 생성한다.")
  void testGenerateBytes() {
    // Given
    final int requiredLength = 16;

    // When
    final byte[] generatedBytes = RandomGenerator.nextBytes(requiredLength);

    // Then
    assertThat(generatedBytes).hasSize(requiredLength);
  }

  @Test
  @DisplayName("주어진 범위 내에서 랜덤 정수를 생성한다.")
  void testGenerateInt() {
    // Given
    final int bound = 10;

    // When
    final int generatedInt = RandomGenerator.nextInt(bound);

    // Then
    assertThat(generatedInt).isNotNegative().isLessThan(bound);
  }
}
