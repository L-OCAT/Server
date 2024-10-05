package com.locat.api.global.mail;

import com.locat.api.global.exception.ApiExceptionType;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private static final String MAILER_CHARSET = StandardCharsets.UTF_8.name();

  private final SesClient sesClient;

  @Value("${service.mail.from-email}")
  private String fromEmail;

  @Override
  public void send(final String to, final String subject, final String content) {
    try {
      this.sesClient.sendEmail(
          request ->
              request
                  .source(this.fromEmail)
                  .destination(d -> d.toAddresses(to))
                  .message(
                      m ->
                          m.subject(s -> s.charset(MAILER_CHARSET).data(subject))
                              .body(b -> b.html(h -> h.charset(MAILER_CHARSET).data(content)))));
    } catch (SesException ex) {
      log.error(
          "SesException occurred while sending email. [Subject: {}] / Reason: {}",
          subject,
          ex.getMessage());
      throw new MailOperationFailedException(ApiExceptionType.FAIL_TO_SEND_EMAIL);
    }
  }
}
