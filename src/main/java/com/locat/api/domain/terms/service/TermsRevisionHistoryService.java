package com.locat.api.domain.terms.service;

import com.locat.api.domain.terms.dto.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;
import java.util.Optional;

public interface TermsRevisionHistoryService {

  void save(TermsRevisionHistory revisionHistory);

  List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType type);

  Optional<TermsRevisionHistory> findByTypeAndVersion(TermsType termsType, Double version);
}
