package com.locat.api.domain.admin.controller;

import com.locat.api.domain.admin.dto.AdminUserFoundItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserLostItemStatDto;
import com.locat.api.domain.admin.dto.AdminUserStatDto;
import com.locat.api.domain.admin.dto.response.AdminUserFoundItemStatResponse;
import com.locat.api.domain.admin.dto.response.AdminUserLostItemStatResponse;
import com.locat.api.domain.admin.dto.response.AdminUserStatResponse;
import com.locat.api.domain.admin.service.AdminStatisticService;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.global.annotation.AdminApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AdminApi
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/statistics")
public class AdminStatisticController {

  private final AdminStatisticService adminStatisticService;

  @GetMapping("/users/{id}")
  public ResponseEntity<BaseResponse<AdminUserStatResponse>> getUserStat(@PathVariable Long id) {
    AdminUserStatDto userStat = this.adminStatisticService.getEndUserStat(id);
    return ResponseEntity.ok(BaseResponse.of(AdminUserStatResponse.from(userStat)));
  }

  @GetMapping("/users/{id}/founds")
  public ResponseEntity<BaseResponse<List<AdminUserFoundItemStatResponse>>> getUserFoundItemStat(
      @PathVariable Long id) {
    List<AdminUserFoundItemStatDto> foundItemStat =
        this.adminStatisticService.getUserFoundItemStat(id);
    return ResponseEntity.ok(
        BaseResponse.of(foundItemStat.stream().map(AdminUserFoundItemStatResponse::from).toList()));
  }

  @GetMapping("/users/{id}/losts")
  public ResponseEntity<BaseResponse<List<AdminUserLostItemStatResponse>>> getUserLostItemStat(
      @PathVariable Long id) {
    List<AdminUserLostItemStatDto> lostItemStat =
        this.adminStatisticService.getUserLostItemStat(id);
    return ResponseEntity.ok(
        BaseResponse.of(lostItemStat.stream().map(AdminUserLostItemStatResponse::from).toList()));
  }
}
