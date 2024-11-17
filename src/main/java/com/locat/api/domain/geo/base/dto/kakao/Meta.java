package com.locat.api.domain.geo.base.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 카카오 API 응답 메타 정보
 *
 * @param totalCount 검색 결과 총 개수
 */
public record Meta(@JsonProperty("total_count") int totalCount) {}
