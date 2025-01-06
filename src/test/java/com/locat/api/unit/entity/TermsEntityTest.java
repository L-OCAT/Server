package com.locat.api.unit.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.locat.api.domain.terms.dto.internal.TermsUpsertDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class TermsEntityTest {

  private static final boolean IS_REQUIRED = true;
  private static final TermsType TYPE = TermsType.PRIVACY_POLICY;
  private static final String TITLE = "개인정보 처리방침";
  private static final String CONTENT = "개인정보 처리방침 내용입니다.";

  @Test
  @DisplayName("static 메서드 create() 또는 update()로 생성한 Terms 객체는 주어진 값 또는 적절한 기본값을 가져야 한다.")
  void testCreateAndUpdate() {
    // Given
    TermsUpsertDto upsertDto1 =
        new TermsUpsertDto(
            TermsType.PRIVACY_POLICY, true, "개인정보 처리방침", "개인정보 처리방침 내용입니다.", "개정 사유");
    TermsUpsertDto upsertDto2 =
        new TermsUpsertDto(
            TermsType.PRIVACY_POLICY, false, "개인정보 처리방침(수정)", "개인정보 처리방침 수정 내용입니다.", "개정 사유2");

    // When & Then
    Terms terms = Terms.create(upsertDto1);
    Terms updatedTerms = terms.update(upsertDto2);
    assertAll(
        // create()로 생성한 Terms 객체
        () -> assertThat(terms).isNotNull(),
        () -> assertThat(terms.getType()).isEqualTo(TYPE),
        () -> assertThat(terms.isRequired()).isEqualTo(IS_REQUIRED),
        () -> assertThat(terms.getTitle()).isEqualTo(TITLE),
        () -> assertThat(terms.getContent()).isEqualTo(CONTENT),
        () -> assertThat(terms.getVersion()).isEqualTo(1.0),
        // update()로 생성한 Terms 객체
        () -> assertThat(updatedTerms).isNotNull(),
        () -> assertThat(updatedTerms.getType()).isEqualTo(TYPE),
        () -> assertThat(updatedTerms.isRequired()).isFalse(),
        () -> assertThat(updatedTerms.getTitle()).isEqualTo("개인정보 처리방침(수정)"),
        () -> assertThat(updatedTerms.getContent()).isEqualTo("개인정보 처리방침 수정 내용입니다."),
        () -> assertThat(updatedTerms.getVersion()).isEqualTo(1.1));
  }
}
