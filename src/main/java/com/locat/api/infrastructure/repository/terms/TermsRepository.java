package com.locat.api.infrastructure.repository.terms;

import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {

  Optional<Terms> findByType(TermsType type);
}
