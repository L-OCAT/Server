package com.locat.api.global.mail;

import static jakarta.mail.Message.RecipientType.TO;

import com.locat.api.global.exception.ApiExceptionType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private static final String MAILER_CHARSET = StandardCharsets.UTF_8.name();

  /**
   * 메일의 Content-Type의 subtype을 나타냅니다. <i>{@code MimeMessage.setText()} 메서드 내부에서 "text/{subtype}"
   * 형식으로 사용됩니다.</i>
   */
  private static final String MAILER_SUBTYPE = "html";

  private final JavaMailSender mailSender;

  @Override
  public void send(final String to, final String subject, final String content) {
    final MimeMessage message = mailSender.createMimeMessage();
    try {
      message.addRecipient(TO, new InternetAddress(to));
      message.setSubject(subject, MAILER_CHARSET);
      message.setText(content, MAILER_CHARSET, MAILER_SUBTYPE);
      this.mailSender.send(message);
    } catch (MessagingException e) {
      throw new MailOperationFailedException(ApiExceptionType.FAIL_TO_CONSTRUCT_EMAIL);
    } catch (MailException e) {
      throw new MailOperationFailedException(ApiExceptionType.FAIL_TO_SEND_EMAIL);
    }
  }
}
