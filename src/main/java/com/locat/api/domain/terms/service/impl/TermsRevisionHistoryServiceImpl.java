package com.locat.api.domain.terms.service.impl;

import com.locat.api.domain.terms.dto.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsRevisionHistoryService;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import com.locat.api.infra.persistence.terms.TermsRevisionHistoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsRevisionHistoryServiceImpl implements TermsRevisionHistoryService {

  private final TermsRevisionHistoryRepository revisionHistoryRepository;
  private final TermsRevisionHistoryQRepository revisionHistoryQRepository;

  @Override
  public void save(TermsRevisionHistory revisionHistory) {
    this.revisionHistoryRepository.save(revisionHistory);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType) {
    return this.revisionHistoryQRepository.findCompactHistoriesByType(termsType);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TermsRevisionHistory> findByTypeAndVersion(TermsType termsType, Double version) {
    return this.revisionHistoryRepository.findByTypeAndVersion(termsType, version);
  }
}
