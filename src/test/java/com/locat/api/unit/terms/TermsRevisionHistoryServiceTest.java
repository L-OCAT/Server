package com.locat.api.unit.terms;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.impl.TermsRevisionHistoryServiceImpl;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class TermsRevisionHistoryServiceTest {

  @InjectMocks private TermsRevisionHistoryServiceImpl service;
  @Mock private TermsRevisionHistoryRepository revisionHistoryRepository;
  @Mock private TermsRevisionHistoryQRepository revisionHistoryQRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("약관 개정 이력 저장에 성공한다.")
  void testSave() {
    // Given
    TermsRevisionHistory revisionHistory = mock(TermsRevisionHistory.class);

    // When & Then
    assertThatCode(() -> this.service.save(revisionHistory)).doesNotThrowAnyException();
    then(this.revisionHistoryRepository).should().save(revisionHistory);
  }

  @Test
  @DisplayName("특정 약관 유형의 약관 개정 이력을 조회한다.")
  void testFindCompactHistoriesByType() {
    // Given
    TermsType termsType = TermsType.TERMS_OF_SERVICE;

    // When & Then
    assertThatCode(() -> this.service.findCompactHistoriesByType(termsType))
        .doesNotThrowAnyException();
    then(this.revisionHistoryQRepository).should().findCompactHistoriesByType(termsType);
  }

  @Test
  @DisplayName("특정 약관 유형과 버전의 약관 개정 이력을 조회한다.")
  void testFindByTypeAndVersion() {
    // Given
    TermsType termsType = TermsType.TERMS_OF_SERVICE;
    Double version = 1.0;

    // When & Then
    assertThatCode(() -> this.service.findByTypeAndVersion(termsType, version))
        .doesNotThrowAnyException();
    then(this.revisionHistoryRepository).should().findByTypeAndVersion(termsType, version);
  }
}
