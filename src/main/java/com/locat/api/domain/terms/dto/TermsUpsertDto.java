package com.locat.api.domain.terms.dto;

import com.locat.api.domain.terms.dto.request.TermsUpsertRequest;
import com.locat.api.domain.terms.entity.TermsType;

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
