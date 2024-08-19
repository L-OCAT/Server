package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.global.utils.RandomCodeGenerator;
import org.junit.jupiter.api.Test;

class RandomCodeGeneratorTest {

  private static final String CHARACTERS_FOR_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  @Test
  void testGenerate() {
    // Given
    final int requiredLength = 6;

    // When
    final String generatedCode = RandomCodeGenerator.generate(requiredLength);

    // Then
    assertThat(generatedCode).hasSize(requiredLength);
    assertThat(generatedCode.chars().allMatch(c -> CHARACTERS_FOR_CODE.indexOf(c) >= 0)).isTrue();
  }
}
