package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.geo.base.dto.kakao.AddressDocument;
import jakarta.persistence.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "geo_item_address",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_geo_item_address",
          columnNames = {"item_id", "item_type"})
    })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeoItemAddress extends BaseEntity {

  @Serial private static final long serialVersionUID = 2024111501L;

  @Transient private static final String ADDRESS_DELIMITER = "-";
  @Transient private static final String EMPTY_PLACEHOLDER = "";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, columnDefinition = "INT UNSIGNED")
  private Long id;

  @Column(name = "item_id", nullable = false, columnDefinition = "INT UNSIGNED")
  private Long itemId;

  @Enumerated(EnumType.STRING)
  @Column(name = "item_type", nullable = false)
  private GeoItemType itemType;

  @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
  private BigDecimal latitude;

  @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
  private BigDecimal longitude;

  @Column(name = "region1", nullable = false, length = 100)
  private String region1;

  @Column(name = "region2", nullable = false, length = 100)
  private String region2;

  @Column(name = "region3", nullable = false, length = 100)
  private String region3;

  @Column(name = "lot_number_address", nullable = false)
  private String lotNumberAddress;

  @Column(name = "road_address")
  private String roadAddress;

  @Column(name = "building_name", length = 100)
  private String buildingName;

  public static GeoItemAddress of(
      GeoItemType geoItemType, GeoItem geoItem, AddressDocument[] addresses) {
    AddressDocument.RoadAddress roadAddressDoc = addresses[0].roadAddress();
    AddressDocument.Address lotNumberAddressDoc = addresses[0].address();
    return GeoItemAddress.builder()
        .itemId(geoItem.getId())
        .itemType(geoItemType)
        .latitude(BigDecimal.valueOf(geoItem.getLocation().getX()))
        .longitude(BigDecimal.valueOf(geoItem.getLocation().getY()))
        .region1(lotNumberAddressDoc.region1depthName())
        .region2(lotNumberAddressDoc.region2depthName())
        .region3(lotNumberAddressDoc.region3depthName())
        .lotNumberAddress(formatLotAddress(lotNumberAddressDoc))
        .roadAddress(roadAddressDoc.addressName())
        .buildingName(roadAddressDoc.buildingName())
        .build();
  }

  public static String formatLotAddress(AddressDocument.Address addressDocument) {
    return addressDocument
        .mainAddressNo()
        .concat(ADDRESS_DELIMITER)
        .concat(Objects.requireNonNullElse(addressDocument.subAddressNo(), EMPTY_PLACEHOLDER));
  }
}
