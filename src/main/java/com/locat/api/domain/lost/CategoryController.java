package com.locat.api.domain.lost;

import com.locat.api.domain.core.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<BaseResponse<?>> findAll() {
    return ResponseEntity.ok().build();
  }
}
