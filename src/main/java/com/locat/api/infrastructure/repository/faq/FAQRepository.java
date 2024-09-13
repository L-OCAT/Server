package com.locat.api.infrastructure.repository.faq;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.entity.FaqType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {

  List<FAQ> findAllByType(FaqType type);
}
