package com.locat.api.domain.faq.service;

import com.locat.api.domain.faq.entity.FAQ;
import java.util.List;

public interface FaqService {

  List<FAQ> findAll();
}
