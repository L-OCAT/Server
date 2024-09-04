package com.locat.api.domain.terms.dto;

import com.locat.api.domain.terms.entity.TermsType;

public record TermsRegisterDto(TermsType type, String title, String content) {

  public static TermsRegisterDto fromRequest(TermsRegisterRequest request) {
    return new TermsRegisterDto(
        TermsType.valueOf(request.type()), request.title(), request.content());
  }
}
