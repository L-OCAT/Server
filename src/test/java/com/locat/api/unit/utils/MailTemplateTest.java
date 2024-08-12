package com.locat.api.unit.utils;

import com.locat.api.global.mail.MailTemplate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MailTemplateTest {

  @Test
  void testCreateMailVerifyMessage() {
    // Given
    String verificationCode = "A1B2C3";

    // When
    String message = MailTemplate.createMailVerifyMessage(verificationCode);

    // Then
    assertThat(message).contains(verificationCode);
    assertThat(message).contains("이메일 본인 인증 코드");
  }
}
