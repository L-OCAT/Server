package com.locat.api.domain.geo.lost;

import com.locat.api.domain.core.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/losts")
@PreAuthorize("isAuthenticated()")
public class LostItemController {

  @PostMapping
  public ResponseEntity<BaseResponse<?>> register() {
    return ResponseEntity.ok(BaseResponse.of("register lost item"));
  }
}
