package com.locat.api.domain.terms.service.impl;

import com.locat.api.domain.terms.dto.TermsUpsertDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsRevisionHistoryService;
import com.locat.api.domain.terms.service.TermsService;
import com.locat.api.infrastructure.repository.terms.TermsRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

  private final TermsRepository termsRepository;
  private final TermsRevisionHistoryService revisionHistoryService;

  @Override
  public Terms upsert(TermsUpsertDto upsertDto) {
    Terms terms =
        this.termsRepository
            .findByType(upsertDto.type())
            .map(exsting -> exsting.update(upsertDto))
            .orElse(Terms.create(upsertDto));

    this.revisionHistoryService.save(TermsRevisionHistory.of(terms, upsertDto.revisionNote()));
    return this.termsRepository.save(terms);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Terms> findByType(TermsType termsType) {
    return this.termsRepository.findByType(termsType);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Terms> findAll() {
    return this.termsRepository.findAll();
  }
}
