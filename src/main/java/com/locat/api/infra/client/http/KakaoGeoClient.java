package com.locat.api.infra.client.http;

import com.locat.api.domain.geo.base.dto.kakao.AddressResponse;
import com.locat.api.infra.client.http.config.KakaoGeoClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoGeoClient",
    url = "https://dapi.kakao.com/v2/local",
    configuration = KakaoGeoClientConfig.class)
public interface KakaoGeoClient {

  /**
   * 좌표값을 주소로 변환
   *
   * @param latitude X 좌표값, 경위도인 경우 경도
   * @param longitude Y 좌표값, 경위도인 경우 위도
   * @return 주소
   * @apiNote 입력 좌표계 기본값은 WGS84
   */
  @GetMapping("/geo/coord2address")
  AddressResponse getAddress(
      @RequestParam("x") double longitude, @RequestParam("y") double latitude);
}
