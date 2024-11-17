package com.locat.api.domain.terms.dto.internal;

import com.locat.api.domain.terms.dto.request.TermsUpsertRequest;
import com.locat.api.domain.terms.entity.TermsType;

/**
 * 약관 등록/수정 DTO
 *
 * @param type 약관 유형
 * @param isRequired 필수 여부
 * @param title 제목
 * @param content 내용
 * @param revisionNote 개정 사유
 */
public record TermsUpsertDto(
    TermsType type, boolean isRequired, String title, String content, String revisionNote) {

  public static TermsUpsertDto fromRequest(TermsUpsertRequest request) {
    return new TermsUpsertDto(
        TermsType.valueOf(request.type()),
        request.isRequired(),
        request.title(),
        request.content(),
        request.revisionNote());
  }
}
