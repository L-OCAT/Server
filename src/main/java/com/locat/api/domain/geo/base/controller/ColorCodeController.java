package com.locat.api.domain.geo.base.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.geo.base.dto.response.ColorCodeResponse;
import com.locat.api.domain.geo.base.service.ColorCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/color-codes")
public class ColorCodeController {

  private final ColorCodeService colorCodeService;

  @GetMapping
  public ResponseEntity<BaseResponse<List<ColorCodeResponse>>> findAll() {
    List<ColorCodeResponse> responses =
        this.colorCodeService.findAll().stream().map(ColorCodeResponse::from).toList();
    return ResponseEntity.ok(BaseResponse.of(responses));
  }
}
