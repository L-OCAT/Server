package com.locat.api.infrastructure.repository.terms;

import com.locat.api.domain.terms.entity.TermsRevisionHistory;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRevisionHistoryRepository extends JpaRepository<TermsRevisionHistory, Long> {

  Optional<TermsRevisionHistory> findByTypeAndVersion(TermsType termsType, Double version);
}
