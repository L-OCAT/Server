package com.locat.api.domain.admin.controller;

import com.locat.api.domain.admin.dto.internal.AdminUserFoundItemStatDto;
import com.locat.api.domain.admin.dto.internal.AdminUserLostItemStatDto;
import com.locat.api.domain.admin.dto.response.*;
import com.locat.api.domain.admin.service.AdminStatisticService;
import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.global.security.annotation.AdminApi;
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

  @GetMapping("/dashboard/summary")
  public ResponseEntity<BaseResponse<AdminDashboardSummaryResponse>> getServiceSummary() {
    AdminDashboardSummaryResponse summary =
        AdminDashboardSummaryResponse.from(this.adminStatisticService.getSummary());
    return ResponseEntity.ok(BaseResponse.of(summary));
  }

  @GetMapping("/dashboard/items/monthly")
  public ResponseEntity<BaseResponse<AdminMontlyItemStatResponse>> getMonthlyItemStat() {
    AdminMontlyItemStatResponse summary =
        AdminMontlyItemStatResponse.from(this.adminStatisticService.getMonthlyItemStat());
    return ResponseEntity.ok(BaseResponse.of(summary));
  }

  @GetMapping("/dashboard/items/by-categories")
  public ResponseEntity<BaseResponse<AdminItemStatByCateogoryResponse>> getItemSummaryByCategory() {
    AdminItemStatByCateogoryResponse summary =
        AdminItemStatByCateogoryResponse.from(this.adminStatisticService.getStatByCategory());
    return ResponseEntity.ok(BaseResponse.of(summary));
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<BaseResponse<AdminUserStatResponse>> getUserStat(@PathVariable Long id) {
    AdminUserStatResponse userStat =
        AdminUserStatResponse.from(this.adminStatisticService.getEndUserStat(id));
    return ResponseEntity.ok(BaseResponse.of(userStat));
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
