package com.locat.api.global.mail;

public interface MailService {

  /**
   * 메일을 전송합니다.
   *
   * @param to 수신자
   * @param subject 제목
   * @param content 내용
   * @throws MailOperationFailedException 메일 본문 생성 실패 또는 전송 실패 시
   */
  void send(final String to, final String subject, final String content);
}
