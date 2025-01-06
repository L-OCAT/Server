package com.locat.api.global.event;

import java.time.LocalDateTime;
import lombok.Builder;

/**
 * 관리자 API 요청에 대한 감사(Audit) 이벤트
 *
 * @param email 요청 관리자명(e-mail)
 * @param superAdminOnly 최고 관리자 전용 API 여부
 * @param method HTTP 메서드
 * @param requestUri 요청 URI
 * @param httpStatus 응답 HTTP 상태 코드
 * @param isSuccessful 처리 성공 여부
 * @param remoteAddress 요청자 IP 주소
 * @param userAgent 요청자 User-Agent
 * @param timestamp 이벤트 발생 일시
 */
@Builder
public record AdminAuditEvent(
    String email,
    boolean superAdminOnly,
    String method,
    String requestUri,
    int httpStatus,
    boolean isSuccessful,
    String remoteAddress,
    String userAgent,
    LocalDateTime timestamp) {}
