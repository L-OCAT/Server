package com.locat.api.infra.persistence.terms;

import com.locat.api.domain.terms.dto.internal.TermsRevisionCompactHistoryDto;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;

public interface TermsRevisionHistoryQRepository {

  List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType);
}
