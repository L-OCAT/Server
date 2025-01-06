package com.locat.api.domain.geo.base.service;

import com.locat.api.domain.geo.base.entity.ColorCode;
import java.util.List;
import java.util.Optional;

public interface ColorCodeService {

  /**
   * 색상 코드 ID로 색상 코드를 조회합니다.
   *
   * @param id 색상 코드 ID
   * @return 색상 코드, 존재하지 않을 경우 {@link Optional#empty()}
   */
  Optional<ColorCode> findById(final Long id);

  /**
   * 모든 색상 코드를 조회합니다.
   *
   * @return 색상 코드 목록
   */
  List<ColorCode> findAll();
}
