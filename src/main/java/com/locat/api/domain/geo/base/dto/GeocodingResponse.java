package com.locat.api.domain.geo.base.dto;

/** 좌표 변환 API 응답 인터페이스 */
public interface GeocodingResponse {

  /**
   * 응답이 비어있는지 확인
   *
   * @return 응답이 비어있으면 {@code true}, 그렇지 않으면 {@code false}
   */
  boolean isEmpty();
}
