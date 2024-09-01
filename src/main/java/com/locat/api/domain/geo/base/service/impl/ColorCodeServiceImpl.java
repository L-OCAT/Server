package com.locat.api.domain.geo.base.service.impl;

import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.service.ColorCodeService;
import com.locat.api.infrastructure.repository.geo.base.ColorCodeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ColorCodeServiceImpl implements ColorCodeService {

  private final ColorCodeRepository colorCodeRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<ColorCode> findById(Long id) {
    return this.colorCodeRepository.findById(id);
  }
}
