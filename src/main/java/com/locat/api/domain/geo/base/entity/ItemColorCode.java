package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.geo.base.dto.GeoItemType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "item_color_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemColorCode extends BaseEntity {

  @EmbeddedId private ItemColorCodeId id;

  public static ItemColorCode of(GeoItemType itemType, Long itemId, Long colorId) {
    ItemColorCodeId itemColorCodeId = ItemColorCodeId.of(itemType, itemId, colorId);
    return new ItemColorCode(itemColorCodeId);
  }
}
