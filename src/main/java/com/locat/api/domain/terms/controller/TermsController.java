package com.locat.api.domain.terms.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.terms.dto.TermsRegisterDto;
import com.locat.api.domain.terms.dto.TermsRegisterRequest;
import com.locat.api.domain.terms.dto.TermsResponse;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/terms")
@PreAuthorize("isAuthenticated()")
public class TermsController {

  private final TermsService termsService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<TermsResponse>> create(
      @RequestBody @Valid final TermsRegisterRequest request) {
    TermsResponse response =
        TermsResponse.fromEntity(this.termsService.register(TermsRegisterDto.fromRequest(request)));
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping
  public ResponseEntity<BaseResponse<List<TermsResponse>>> findAllLatest() {
    List<TermsResponse> response =
        this.termsService.findAllLatest().stream().map(TermsResponse::fromEntity).toList();
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping("/{type}")
  public ResponseEntity<BaseResponse<TermsResponse>> findLatestByType(
      @PathVariable final String type) {
    Terms terms =
        this.termsService
            .findLatestByType(TermsType.fromValue(type))
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_TERMS));
    TermsResponse response = TermsResponse.fromEntity(terms);
    return ResponseEntity.ok(BaseResponse.of(response));
  }
}
