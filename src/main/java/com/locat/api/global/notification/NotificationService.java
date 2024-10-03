package com.locat.api.global.notification;

public interface NotificationService {

  /**
   * 모든 사용자에게 메세지를 브로드캐스트합니다.
   *
   * @param subject 메시지 제목
   * @param message 메시지 내용
   * @return 발송된 메시지의 고유 ID
   * @throws NotificationException 메세지 발송에 실패한 경우
   */
  String broadcast(String subject, String message);

  /**
   * 특정 사용자에게 등록된 모든 디바이스에 Push Notification을 발송합니다.
   *
   * @param userId 사용자 ID
   * @param subject 메시지 제목
   * @param message 메시지 내용
   * @return 발송된 메시지의 고유 ID 목록(","로 구분)
   * @throws NotificationException 사용자에게 등록된 디바이스가 없거나, 메세지 발송에 실패한 경우
   */
  String unicast(Long userId, String subject, String message);
}
