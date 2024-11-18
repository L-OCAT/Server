package com.locat.api.domain.terms.service;

import com.locat.api.domain.terms.dto.internal.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;
import java.util.Optional;

public interface TermsRevisionHistoryService {

  /**
   * 약관 개정 이력 정보를 저장합니다.
   *
   * @param revisionHistory 저장할 개정 이력 정보
   */
  void save(TermsRevisionHistory revisionHistory);

  /**
   * (본문 정보를 제외한) 약관 개정 이력 정보를 타입별로 조회합니다.
   *
   * @param type 조회할 약관 타입
   * @return 약관 개정 이력 정보 목록
   */
  List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType type);

  /**
   * 지정한 타입/버전의 약관 개정 이력 정보를 조회합니다.
   *
   * @param termsType 조회할 약관 타입
   * @param version 조회할 약관 버전
   * @return 약관 개정 이력 정보, 존재하지 않을 경우 {@link Optional#empty()}
   */
  Optional<TermsRevisionHistory> findByTypeAndVersion(TermsType termsType, Double version);
}
