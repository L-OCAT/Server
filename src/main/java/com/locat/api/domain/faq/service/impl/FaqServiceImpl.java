package com.locat.api.domain.faq.service.impl;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.entity.FaqType;
import com.locat.api.domain.faq.service.FaqService;
import com.locat.api.infra.persistence.faq.FAQRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

  private final FAQRepository faqRepository;

  @Override
  @Transactional(readOnly = true)
  public List<FAQ> findAllByType(FaqType type) {
    return this.faqRepository.findAllByType(type);
  }
}
