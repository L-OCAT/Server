package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.global.utils.RandomGenerator;
import org.junit.jupiter.api.Test;

class RandomCodeGeneratorTest {

  private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  @Test
  void testGenerateCode() {
    // Given
    final int requiredLength = 6;

    // When
    final String generatedCode = RandomGenerator.generateRandomCode(requiredLength);

    // Then
    assertThat(generatedCode).hasSize(requiredLength);
    assertThat(generatedCode.chars().allMatch(c -> CHARACTERS_FOR_CODE.indexOf(c) >= 0)).isTrue();
  }

  @Test
  void testGenerateBytes() {
    // Given
    final int requiredLength = 16;

    // When
    final byte[] generatedBytes = RandomGenerator.generateRandomBytes(requiredLength);

    // Then
    assertThat(generatedBytes).hasSize(requiredLength).doesNotContain((byte) 0);
  }
}
