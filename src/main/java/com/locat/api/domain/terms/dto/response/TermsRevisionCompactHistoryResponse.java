package com.locat.api.domain.terms.dto.response;

import com.locat.api.domain.terms.dto.TermsRevisionCompactHistoryDto;

/**
 * 약관 개정 이력 응답 DTO
 *
 * @param version 약관 버전
 * @param revisionNote 변경 사유
 * @param createdAt 개정일시
 */
public record TermsRevisionCompactHistoryResponse(
    String version, String revisionNote, String createdAt) {

  public static TermsRevisionCompactHistoryResponse from(
      TermsRevisionCompactHistoryDto historyDto) {
    return new TermsRevisionCompactHistoryResponse(
        historyDto.version().toString(),
        historyDto.revisionNote(),
        historyDto.createdAt().toString());
  }
}
