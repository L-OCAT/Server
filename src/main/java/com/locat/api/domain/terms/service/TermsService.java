package com.locat.api.domain.terms.service;

import com.locat.api.domain.terms.dto.TermsRegisterDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;
import java.util.Optional;

public interface TermsService {

  /**
   * 새로 또는 수정된 약관을 등록합니다.
   *
   * @param registerDto 등록할 약관 정보에 대한 DTO
   * @return 등록된 약관 정보
   * @apiNote {@code version}은 자동 부여됩니다.
   */
  Terms register(final TermsRegisterDto registerDto);

  /**
   * 지정한 타입의 최신 버전 약관을 조회합니다.
   *
   * @param termsType 약관 타입
   * @return 약관 정보
   */
  Optional<Terms> findLatestByType(final TermsType termsType);

  /**
   * 최신 버전의 모든 약관을 조회합니다.
   *
   * @return 약관 목록
   */
  List<Terms> findAllLatest();
}
