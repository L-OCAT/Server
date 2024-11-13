package com.locat.api.domain.terms.dto;

import java.time.LocalDateTime;

/**
 * 약관 개정 이력(Compact) DTO
 *
 * @param version 버전
 * @param revisionNote 개정 사유
 * @param createdAt 개정 일시
 */
public record TermsRevisionCompactHistoryDto(
    Double version, String revisionNote, LocalDateTime createdAt) {}
