package com.locat.api.domain.faq.service;

import com.locat.api.domain.faq.entity.FAQ;
import com.locat.api.domain.faq.entity.FaqType;
import java.util.List;

public interface FaqService {

  /**
   * FAQ 타입별 전체 조회
   *
   * @param type 조회할 FAQ 타입
   * @return 해당 타입의 FAQ 리스트
   */
  List<FAQ> findAllByType(FaqType type);
}
