package com.locat.api.domain.faq.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.faq.dto.FaqResponse;
import com.locat.api.domain.faq.service.FaqService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/faqs")
public class FaqController {

  private final FaqService faqService;

  @GetMapping
  public ResponseEntity<BaseResponse<List<FaqResponse>>> findAll() {
    List<FaqResponse> response =
        this.faqService.findAll().stream().map(FaqResponse::fromEntity).toList();
    return ResponseEntity.ok(BaseResponse.of(response));
  }
}
