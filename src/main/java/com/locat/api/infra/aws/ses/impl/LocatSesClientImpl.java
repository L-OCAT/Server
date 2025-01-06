package com.locat.api.infra.aws.ses.impl;

import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.infra.aws.AbstractLocatAwsClient;
import com.locat.api.infra.aws.config.AwsProperties;
import com.locat.api.infra.aws.exception.MailOperationException;
import com.locat.api.infra.aws.ses.LocatSesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
@Slf4j
public class LocatSesClientImpl extends AbstractLocatAwsClient implements LocatSesClient {

  private final SesClient sesClient;

  public LocatSesClientImpl(AwsProperties awsProperties, SesClient sesClient) {
    super(awsProperties);
    this.sesClient = sesClient;
  }

  @Override
  public void send(final String to, final String subject, final String content) {
    try {
      SendEmailRequest request =
          SendEmailRequest.builder()
              .source(this.awsProperties.ses().from())
              .destination(d -> d.toAddresses(to))
              .message(
                  m ->
                      m.subject(s -> s.charset(AwsProperties.Ses.MAILER_CHARSET).data(subject))
                          .body(
                              b ->
                                  b.html(
                                      h ->
                                          h.charset(AwsProperties.Ses.MAILER_CHARSET)
                                              .data(content))))
              .build();
      this.sesClient.sendEmail(request);
    } catch (SesException ex) {
      log.error(
          "SesException occurred while sending email. [Subject: {}] / Reason: {}",
          subject,
          ex.getMessage());
      throw new MailOperationException(ApiExceptionType.FAIL_TO_SEND_EMAIL);
    }
  }
}
