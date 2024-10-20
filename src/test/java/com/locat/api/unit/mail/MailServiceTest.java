package com.locat.api.unit.mail;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.locat.api.global.mail.MailOperationFailedException;
import com.locat.api.global.mail.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

class MailServiceTest {

  private static final String TEST_FROM_EMAIL = "test@locat.kr";

  @InjectMocks private MailServiceImpl mailService;
  @Mock private SesClient sesClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(this.mailService, "fromEmail", TEST_FROM_EMAIL, String.class);
  }

  @Test
  @DisplayName("이메일을 성공적으로 발송해야 한다.")
  void shouldSendEmailSuccessfully() {
    // Given
    String to = "recipient@locat.kr";
    String subject = "제목";
    String content = "내용";

    // When
    this.mailService.send(to, subject, content);

    // Then
    verify(this.sesClient).sendEmail(any(SendEmailRequest.class));
  }

  @Test
  @DisplayName("SES 클라이언트가 예외를 던지면 메일 발송에 실패해야 한다.")
  void shouldFailToSendEmailWhenException() {
    // Given
    String to = "recipient@locat.kr";
    String subject = "제목";
    String content = "내용";
    doThrow(SesException.class).when(this.sesClient).sendEmail(any(SendEmailRequest.class));

    // When & Then
    assertThatThrownBy(() -> this.mailService.send(to, subject, content))
        .isExactlyInstanceOf(MailOperationFailedException.class)
        .hasMessage("Failed to send mail");
  }
}
