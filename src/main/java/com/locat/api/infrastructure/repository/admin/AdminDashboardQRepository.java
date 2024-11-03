package com.locat.api.infrastructure.repository.admin;

import com.locat.api.domain.admin.dto.AdminDashboardSummaryDto;
import com.locat.api.domain.admin.dto.AdminItemStatByCategoryDto;
import com.locat.api.domain.admin.dto.AdminMonthlyItemStatDto;
import java.util.List;

/** 관리자 대시보드 정보 QueryDSL Repository */
public interface AdminDashboardQRepository {

  AdminDashboardSummaryDto getSummary();

  List<AdminItemStatByCategoryDto> getStatByCategory();

  AdminMonthlyItemStatDto getMonthlyItemStat();
}
