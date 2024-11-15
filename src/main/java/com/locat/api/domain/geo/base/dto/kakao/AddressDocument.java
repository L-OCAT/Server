package com.locat.api.domain.geo.base.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 주소 정보 DTO
 *
 * @param roadAddress 도로명 주소
 * @param address 지번 주소
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddressDocument(RoadAddress roadAddress, Address address) {

  /**
   * 지번 주소 정보
   *
   * @param addressName 전체 지번 주소
   * @param region1depthName 시/도 단위
   * @param region2depthName 구 단위
   * @param region3depthName 동 단위
   * @param mountainYn 산지 여부 (Y/N)
   * @param mainAddressNo 지번 본번
   * @param subAddressNo 지번 부번(없을 경우 "")
   */
  public record Address(
      @JsonProperty("address_name") String addressName,
      @JsonProperty("region_1depth_name") String region1depthName,
      @JsonProperty("region_2depth_name") String region2depthName,
      @JsonProperty("region_3depth_name") String region3depthName,
      @JsonProperty("mountain_yn") String mountainYn,
      @JsonProperty("main_address_no") String mainAddressNo,
      @JsonProperty("sub_address_no") String subAddressNo) {}

  /**
   * 도로명 주소 정보
   *
   * @param addressName 전체 도로명 주소
   * @param region1depthName 시/도 단위
   * @param region2depthName 구 단위
   * @param region3depthName 동 단위
   * @param roadName 도로명
   * @param undergroundYn 지하 여부 (Y/N)
   * @param mainBuildingNo 건물 본번
   * @param subBuildingNo 건물 부번 (없을 경우 "")
   * @param buildingName 건물명
   * @param zoneNo 우편번호
   */
  public record RoadAddress(
      @JsonProperty("address_name") String addressName,
      @JsonProperty("region_1depth_name") String region1depthName,
      @JsonProperty("region_2depth_name") String region2depthName,
      @JsonProperty("region_3depth_name") String region3depthName,
      @JsonProperty("road_name") String roadName,
      @JsonProperty("underground_yn") String undergroundYn,
      @JsonProperty("main_building_no") String mainBuildingNo,
      @JsonProperty("sub_building_no") String subBuildingNo,
      @JsonProperty("building_name") String buildingName,
      @JsonProperty("zone_no") String zoneNo) {}
}
