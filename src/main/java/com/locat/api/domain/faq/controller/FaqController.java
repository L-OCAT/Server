package com.locat.api.domain.faq.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.faq.dto.FaqResponse;
import com.locat.api.domain.faq.entity.FaqType;
import com.locat.api.domain.faq.service.FaqService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/faqs")
public class FaqController {

  private final FaqService faqService;

  @GetMapping("/{type}")
  public ResponseEntity<BaseResponse<List<FaqResponse>>> findAllByType(
      @PathVariable final String type) {
    final FaqType termsType = FaqType.fromValueOrDefault(type);
    List<FaqResponse> response =
        this.faqService.findAllByType(termsType).stream().map(FaqResponse::fromEntity).toList();
    return ResponseEntity.ok(BaseResponse.of(response));
  }
}
