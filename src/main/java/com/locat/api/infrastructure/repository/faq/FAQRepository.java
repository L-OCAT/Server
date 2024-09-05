package com.locat.api.infrastructure.repository.faq;

import com.locat.api.domain.faq.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
