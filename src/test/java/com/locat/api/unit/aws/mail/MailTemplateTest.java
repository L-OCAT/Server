package com.locat.api.unit.aws.mail;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.locat.api.infra.aws.ses.impl.MailTemplate;
import org.junit.jupiter.api.Test;

class MailTemplateTest {

  @Test
  void testCreateMailVerifyMessage() {
    // Given
    String verificationCode = "A1B2C3";

    // When
    String message = MailTemplate.createMailVerifyMessage(verificationCode);

    // Then
    assertThat(message).contains(verificationCode).contains("이메일 본인 인증 코드");
  }
}
