package com.locat.api.domain.terms.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.terms.dto.*;
import com.locat.api.domain.terms.dto.request.TermsUpsertRequest;
import com.locat.api.domain.terms.dto.response.TermsResponse;
import com.locat.api.domain.terms.dto.response.TermsRevisionCompactHistoryResponse;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsRevisionHistoryService;
import com.locat.api.domain.terms.service.TermsService;
import com.locat.api.global.annotation.AdminApi;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/terms")
public class TermsController {

  private final TermsService termsService;
  private final TermsRevisionHistoryService revisionHistoryService;

  @AdminApi(superAdminOnly = true, audit = true)
  @PostMapping
  public ResponseEntity<BaseResponse<TermsResponse>> upsert(
      @RequestBody @Valid final TermsUpsertRequest request) {
    TermsResponse response =
        TermsResponse.toDetailed(this.termsService.upsert(TermsUpsertDto.fromRequest(request)));
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping
  public ResponseEntity<BaseResponse<List<TermsResponse>>> findAll() {
    List<TermsResponse> response =
        this.termsService.findAll().stream().map(TermsResponse::toCompact).toList();
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping("/{type}")
  public ResponseEntity<BaseResponse<TermsResponse>> findByType(@PathVariable final String type) {
    Terms terms =
        this.termsService
            .findByType(TermsType.fromValue(type))
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_TERMS));
    TermsResponse response = TermsResponse.toDetailed(terms);
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping("/{type}/revisions")
  public ResponseEntity<BaseResponse<List<TermsRevisionCompactHistoryResponse>>>
      findRevisionHistoriesByType(@PathVariable final String type) {
    List<TermsRevisionCompactHistoryResponse> response =
        this.revisionHistoryService.findCompactHistoriesByType(TermsType.fromValue(type)).stream()
            .map(TermsRevisionCompactHistoryResponse::from)
            .toList();
    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping("/{type}/revisions/{version}")
  public ResponseEntity<BaseResponse<TermsResponse>> findRevisionByTypeAndVersion(
      @PathVariable final String type, @PathVariable final Double version) {
    TermsResponse response =
        this.revisionHistoryService
            .findByTypeAndVersion(TermsType.fromValue(type), version)
            .map(TermsResponse::toDetailed)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_TERMS));
    return ResponseEntity.ok(BaseResponse.of(response));
  }
}
