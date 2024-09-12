package com.locat.api.domain.terms.service.impl;

import com.locat.api.domain.terms.dto.TermsRegisterDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsService;
import com.locat.api.infrastructure.repository.terms.TermsQRepository;
import com.locat.api.infrastructure.repository.terms.TermsRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

  private static final BigDecimal VERSION_INCREMENT = new BigDecimal("0.1");

  private final TermsRepository termsRepository;
  private final TermsQRepository termsQRepository;

  @Override
  public Terms register(TermsRegisterDto registerDto) {
    BigDecimal previousVersion =
        this.termsRepository
            .findByType(registerDto.type())
            .map(Terms::getVersion)
            .orElse(BigDecimal.ZERO);
    return this.termsRepository.save(
        Terms.from(registerDto, previousVersion.add(VERSION_INCREMENT)));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Terms> findLatestByType(TermsType termsType) {
    return this.termsQRepository.findLatestByType(termsType);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Terms> findAllLatest() {
    return this.termsQRepository.findAllLatest();
  }
}
