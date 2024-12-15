package com.locat.api.domain.geo.base.dto.response;

import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.base.entity.GeoItemAddress;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record GeoItemDetailResponse (
    Long id,
    Long itemId,
    String username,
    String itemType,
    String itemName,
    String imageUrl,
    String status,
    Set<String> colorNames,
    double lat,
    double lng,
    String region1,
    String region2,
    String region3,
    @Nullable String roadAddress,
    @Nullable String buildingName,
    LocalDateTime createdAt) {

    public static GeoItemDetailResponse from(GeoItem geoItem, GeoItemAddress geoItemAddress) {
        return GeoItemDetailResponse.builder()
                .id(geoItemAddress.getId())
                .itemId(geoItem.getId())
                .username(geoItem.getUser().getNickname())
                .itemType(geoItemAddress.getItemType().name())
                .itemName(geoItem.getName())
                .imageUrl(geoItem.getImageUrl())
                .status(geoItem instanceof FoundItem foundItem ? foundItem.getStatusType().name() :
                        geoItem instanceof LostItem lostItem ? lostItem.getStatusType().name() : null)
                .colorNames(geoItem instanceof FoundItem foundItem ? foundItem.getColorNames() :
                        geoItem instanceof LostItem lostItem ? lostItem.getColorNames() : null)
                .lat(geoItemAddress.getLatitude().doubleValue())
                .lng(geoItemAddress.getLongitude().doubleValue())
                .region1(geoItemAddress.getRegion1())
                .region2(geoItemAddress.getRegion2())
                .region3(geoItemAddress.getRegion3())
                .roadAddress(geoItemAddress.getRoadAddress())
                .buildingName(geoItemAddress.getBuildingName())
                .createdAt(geoItem.getCreatedAt())
                .build();
    }
}
