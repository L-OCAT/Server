package com.locat.api.infrastructure.repository.terms;

import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;
import java.util.Optional;

public interface TermsQRepository {

  Optional<Terms> findLatestByType(final TermsType termsType);

  List<Terms> findAllLatest();
}
