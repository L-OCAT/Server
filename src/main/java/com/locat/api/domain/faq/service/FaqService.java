package com.locat.api.domain.faq.service;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.entity.FaqType;
import java.util.List;

public interface FaqService {

  List<FAQ> findAllByType(FaqType type);
}
