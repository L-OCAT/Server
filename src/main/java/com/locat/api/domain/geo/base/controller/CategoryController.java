package com.locat.api.domain.geo.base.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.base.dto.response.CategoryInfoResponse;
import com.locat.api.domain.geo.base.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  /** 전체 카테고리 조회 */
  @GetMapping
  public ResponseEntity<BaseResponse<List<CategoryInfoResponse>>> findAll() {
    List<CategoryInfoResponse> categoryInfoResponses =
        this.categoryService.findAll().stream().map(CategoryInfoResponse::from).toList();
    return ResponseEntity.ok(BaseResponse.of(categoryInfoResponses));
  }
}
