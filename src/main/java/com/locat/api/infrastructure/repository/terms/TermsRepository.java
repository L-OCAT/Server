package com.locat.api.infrastructure.repository.terms;

import com.locat.api.domain.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}
