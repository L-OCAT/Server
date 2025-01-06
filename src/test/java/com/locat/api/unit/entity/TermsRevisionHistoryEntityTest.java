package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class TermsRevisionHistoryEntityTest {

  @Test
  @DisplayName("of() 메서드로 생성한 TermsRevisionHistory 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testOfMethod() {
    // Given
    Terms terms =
        Terms.builder()
            .id(1L)
            .type(TermsType.TERMS_OF_SERVICE)
            .isRequired(true)
            .title("서비스 이용약관")
            .content("서비스 이용약관 내용입니다.")
            .build();
    String revisionNote = "개정 사유";

    // When
    TermsRevisionHistory termsRevisionHistory = TermsRevisionHistory.of(terms, revisionNote);

    // Then
    assertAll(
        () -> assertThat(termsRevisionHistory).isNotNull(),
        () -> assertThat(termsRevisionHistory.getId()).isNull(),
        () -> assertThat(termsRevisionHistory.getType()).isEqualTo(TermsType.TERMS_OF_SERVICE),
        () -> assertThat(termsRevisionHistory.isRequired()).isTrue(),
        () -> assertThat(termsRevisionHistory.getTitle()).isEqualTo("서비스 이용약관"),
        () -> assertThat(termsRevisionHistory.getContent()).isEqualTo("서비스 이용약관 내용입니다."),
        () -> assertThat(termsRevisionHistory.getRevisionNote()).isEqualTo(revisionNote));
  }
}
