package com.locat.api.domain.admin.dto.internal;

import com.locat.api.domain.geo.base.entity.GeoItem;

/**
 * 월별 {@link GeoItem} 등록 상세 정보
 *
 * @param month 월
 * @param count 등록 수
 */
public record MonthlyGeoItemStatistics(String month, int count) {}
