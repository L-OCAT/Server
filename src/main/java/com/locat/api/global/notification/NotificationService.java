package com.locat.api.global.notification;

public interface NotificationService {

  /**
   * 단체 앱 푸시 알림을 전송합니다.
   *
   * @param message 메시지 내용
   * @param subject 메시지 제목
   * @return SNS에 발송된 메시지의 고유 아이디
   */
  String sendGeneralNotification(String message, String subject);

  /**
   * 개인용 앱 푸시 알림을 사용자의 모든 디바이스로 전송합니다.
   *
   * @param userId 사용자 id
   * @param message 메시지 내용
   * @param subject 메시지 제목
   * @return SNS에 발송된 메시지의 고유 아이디 리스트
   */
  String sendUserNotification(Long userId, String message, String subject);
}
