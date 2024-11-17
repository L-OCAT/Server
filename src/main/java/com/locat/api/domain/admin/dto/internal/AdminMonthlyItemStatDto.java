package com.locat.api.domain.admin.dto.internal;

import java.util.List;

public record AdminMonthlyItemStatDto(
    List<String> monthLabels,
    List<MonthlyGeoItemStatistics> lostItemCount,
    List<MonthlyGeoItemStatistics> foundItemCount) {}
