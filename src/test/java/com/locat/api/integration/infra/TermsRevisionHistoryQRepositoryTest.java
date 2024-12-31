package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.terms.dto.internal.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class TermsRevisionHistoryQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private TermsRevisionHistoryQRepository repository;

  @Test
  @DisplayName("주어진 약관 유형에 맞는 약관 개정 이력을 조회할 수 있다.")
  @Transactional(readOnly = true)
  void testFindCompactHistoriesByType() {
    // Given
    var privacyPolicy = TermsType.PRIVACY_POLICY;
    var termsOfService = TermsType.TERMS_OF_SERVICE;

    // When
    var result1 = this.repository.findCompactHistoriesByType(privacyPolicy);
    var result2 = this.repository.findCompactHistoriesByType(termsOfService);

    // Then
    assertThat(result1)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(TermsRevisionCompactHistoryDto.class)
        .allSatisfy(
            dto -> {
              assertThat(dto.version()).isNotNull();
              assertThat(dto.revisionNote()).isNotNull();
              assertThat(dto.createdAt()).isNotNull();
            });
    assertThat(result2)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(TermsRevisionCompactHistoryDto.class)
        .allSatisfy(
            dto -> {
              assertThat(dto.version()).isNotNull();
              assertThat(dto.revisionNote()).isNotNull();
              assertThat(dto.createdAt()).isNotNull();
            });
  }
}
