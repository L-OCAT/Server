package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.geo.base.dto.GeoItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemColorCodeId implements Serializable {

  @Enumerated(EnumType.STRING)
  @Column(name = "item_type", nullable = false)
  private GeoItemType itemType;

  @NotNull @Column(name = "item_id", nullable = false)
  private Long itemId;

  @NotNull @Column(name = "color_id", nullable = false)
  private Long colorId;

  public static ItemColorCodeId of(GeoItemType itemType, Long itemId, Long colorId) {
    return new ItemColorCodeId(itemType, itemId, colorId);
  }
}
