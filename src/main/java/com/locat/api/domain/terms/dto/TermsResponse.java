package com.locat.api.domain.terms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import jakarta.annotation.Nullable;

/**
 * 서비스 약관 응답 DTO
 *
 * @param type 서비스의 {@link TermsType}
 * @param title 약관 제목
 * @param content 본문(리스트 조회 시 null)
 * @param version 약관 버전
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TermsResponse(String type, String title, @Nullable String content, String version) {

  public static TermsResponse toDetailed(Terms terms) {
    return new TermsResponse(
        terms.getType().name(),
        terms.getTitle(),
        terms.getContent(),
        terms.getVersion().toPlainString());
  }

  public static TermsResponse toCompact(Terms terms) {
    return new TermsResponse(
        terms.getType().name(), terms.getTitle(), null, terms.getVersion().toPlainString());
  }
}
