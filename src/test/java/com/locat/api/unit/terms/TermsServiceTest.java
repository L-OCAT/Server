package com.locat.api.unit.terms;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.locat.api.domain.terms.dto.internal.TermsUpsertDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsRevisionHistoryService;
import com.locat.api.domain.terms.service.impl.TermsServiceImpl;
import com.locat.api.infra.persistence.terms.TermsRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

final class TermsServiceTest {

  @InjectMocks private TermsServiceImpl service;
  @Mock private TermsRepository repository;
  @Mock TermsRevisionHistoryService revisionHistoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("해당 약관 유형이 존재하지 않는다면, 새로운 약관을 생성한다.")
  void testUpsert() {
    // Given
    TermsUpsertDto upsertDto =
        new TermsUpsertDto(TermsType.TERMS_OF_SERVICE, true, "제목", "내용", "개정 사항");
    given(this.repository.findByType(TermsType.TERMS_OF_SERVICE)).willReturn(Optional.empty());

    // When & Then
    assertThatCode(() -> this.service.upsert(upsertDto)).doesNotThrowAnyException();
    then(this.repository).should().save(any(Terms.class));
    then(this.revisionHistoryService).should().save(any(TermsRevisionHistory.class));
  }

  @Test
  @DisplayName("해당 약관 유형이 존재한다면, 기존 약관을 수정한다.")
  void testUpsertWhenTermsExist() {
    // Given
    Terms existing =
        Terms.builder()
            .type(TermsType.TERMS_OF_SERVICE)
            .isRequired(true)
            .title("제목")
            .content("내용")
            .version(1.0)
            .build();
    TermsUpsertDto upsertDto =
        new TermsUpsertDto(TermsType.TERMS_OF_SERVICE, true, "제목", "내용", "개정 사항");
    given(this.repository.findByType(TermsType.TERMS_OF_SERVICE)).willReturn(Optional.of(existing));

    // When & Then
    assertThatCode(() -> this.service.upsert(upsertDto)).doesNotThrowAnyException();
    then(this.repository).should().save(any(Terms.class));
    then(this.revisionHistoryService).should().save(any(TermsRevisionHistory.class));
  }

  @Test
  @DisplayName("특정 약관 유형을 조회한다.")
  void testFindByType() {
    // Given
    TermsType termsType = TermsType.TERMS_OF_SERVICE;

    // When & Then
    assertThatCode(() -> this.service.findByType(termsType)).doesNotThrowAnyException();
    then(this.repository).should().findByType(termsType);
  }

  @Test
  @DisplayName("모든 약관을 조회한다.")
  void testFindAll() {
    // Given
    // no given

    // When & Then
    assertThatCode(() -> this.service.findAll()).doesNotThrowAnyException();
    then(this.repository).should().findAll();
  }
}
