package com.locat.api.infrastructure.repository.terms;

import com.locat.api.domain.terms.dto.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;

public interface TermsRevisionHistoryQRepository {

  List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType);
}
