package com.locat.api.domain.faq.service.impl;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.service.FaqService;
import com.locat.api.infrastructure.repository.faq.FAQRepository;
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
  public List<FAQ> findAll() {
    return this.faqRepository.findAll();
  }
}
