package com.locat.api.global.notification;

public interface NotificationService {

    /**
     * 단체 앱 푸시 알림을 전송합니다.
     * @param message 메시지 내용
     * @param subject 메시지 제목
     */
    void sendGeneralNotification(String message, String subject);

    /**
     * 개인용 앱 푸시 알림을 전송합니다.
     * @param userId 사용자 id
     * @param message 메시지 내용
     * @param subject 메시지 제목
     */
    void sendUserNotification(Long userId, String message, String subject);
}
