package com.locat.api.unit.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.global.utils.HashingUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashingUtilsTest {

  @Test
  @DisplayName("입력 값을 해싱하여 반환한다.")
  void testHash() {
    // Given
    String value1 = "hello";
    String expected1 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";

    String value2 = "";
    String expected2 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    // When
    String actual1 = HashingUtils.hash(value1);
    String actual2 = HashingUtils.hash(value2);

    // Then
    assertThat(actual1).isEqualTo(expected1);
    assertThat(actual2).isEqualTo(expected2);
  }
}
