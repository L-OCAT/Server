package com.locat.api.domain.terms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import jakarta.annotation.Nullable;
import lombok.Builder;

/**
 * 서비스 약관 응답 DTO
 *
 * @param id 약관 ID
 * @param isRequired 필수 여부
 * @param type 서비스의 {@link TermsType}
 * @param title 약관 제목
 * @param content 본문(리스트 조회 시 null)
 * @param version 약관 버전
 * @param createdAt 약관 생성일
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TermsResponse(
    Long id,
    boolean isRequired,
    String type,
    String title,
    @Nullable String content,
    double version,
    String createdAt) {

  public static TermsResponse toDetailed(Terms terms) {
    return TermsResponse.builder()
        .id(terms.getId())
        .isRequired(terms.isRequired())
        .type(terms.getType().name())
        .title(terms.getTitle())
        .content(terms.getContent())
        .version(terms.getVersion())
        .createdAt(terms.getCreatedAt().toString())
        .build();
  }

  public static TermsResponse toDetailed(TermsRevisionHistory revisionHistory) {
    return TermsResponse.builder()
        .id(revisionHistory.getId())
        .isRequired(revisionHistory.isRequired())
        .type(revisionHistory.getType().name())
        .title(revisionHistory.getTitle())
        .content(revisionHistory.getContent())
        .version(revisionHistory.getVersion())
        .createdAt(revisionHistory.getCreatedAt().toString())
        .build();
  }

  public static TermsResponse toCompact(Terms terms) {
    return TermsResponse.builder()
        .id(terms.getId())
        .type(terms.getType().name())
        .isRequired(terms.isRequired())
        .title(terms.getTitle())
        .version(terms.getVersion())
        .createdAt(terms.getCreatedAt().toString())
        .build();
  }
}
