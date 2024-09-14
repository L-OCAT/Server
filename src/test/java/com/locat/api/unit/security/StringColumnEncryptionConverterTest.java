package com.locat.api.unit.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.global.security.StringColumnEncryptionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StringColumnEncryptionConverterTest {

  @InjectMocks private StringColumnEncryptionConverter converter;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(
        this.converter, "encryptionKey", "testEncryptionKey123", String.class);
    MockitoAnnotations.openMocks(this);
    this.converter.init();
  }

  @Test
  @DisplayName("평문 암호화에 성공해야 한다.")
  void encryptStringtoDatabaseColumn() {
    // Given
    String plainText = "Hello, World!";

    // When
    String encrypted = this.converter.convertToDatabaseColumn(plainText);

    // Then
    assertThat(encrypted).isNotNull();
    assertThat(encrypted).isNotEqualTo(plainText);
  }

  @Test
  @DisplayName("암호문 복호화에 성공해야 한다.")
  void decryptDatabaseColumnToPlain() {
    // Given
    String expected = "Hello, World!";
    String encryptedExpect = this.converter.convertToDatabaseColumn(expected);

    // When
    String actual = this.converter.convertToEntityAttribute(encryptedExpect);

    // Then
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("null이 주어지는 경우 null을 반환해야 한다.")
  void returnNullWhenNullIsGiven() {
    // Given & When
    String encrypted = this.converter.convertToDatabaseColumn(null);
    String decrypted = this.converter.convertToEntityAttribute(null);

    // Then
    assertThat(encrypted).isNull();
    assertThat(decrypted).isNull();
  }
}
