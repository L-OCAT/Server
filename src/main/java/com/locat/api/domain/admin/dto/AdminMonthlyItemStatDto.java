package com.locat.api.domain.admin.dto;

import com.locat.api.domain.admin.dto.inner.MonthlyGeoItemStatistics;
import java.util.List;

public record AdminMonthlyItemStatDto(
    List<String> monthLabels,
    List<MonthlyGeoItemStatistics> lostItemCount,
    List<MonthlyGeoItemStatistics> foundItemCount) {}
