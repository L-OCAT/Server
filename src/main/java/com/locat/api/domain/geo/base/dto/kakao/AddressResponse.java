package com.locat.api.domain.geo.base.dto.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.locat.api.domain.geo.base.dto.GeocodingResponse;

/**
 * 카카오 API 좌표 -> 주소 변환 API 응답 DTO
 *
 * @param meta 메타 정보
 * @param documents 주소 정보 목록
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddressResponse(Meta meta, AddressDocument[] documents) implements GeocodingResponse {

  @Override
  public boolean isEmpty() {
    return this.meta.totalCount() == 0;
  }
}
