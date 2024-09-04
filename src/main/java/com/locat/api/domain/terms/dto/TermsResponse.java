package com.locat.api.domain.terms.dto;

import com.locat.api.domain.terms.entity.Terms;

public record TermsResponse(String type, String title, String content, String version) {
  public static TermsResponse fromEntity(Terms terms) {
    return new TermsResponse(
        terms.getType().name(),
        terms.getTitle(),
        terms.getContent(),
        terms.getVersion().toPlainString());
  }
}
