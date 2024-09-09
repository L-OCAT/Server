package com.locat.api.domain.geo.found.entity;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(
    name = "found_item",
    indexes = {@Index(name = "idx_found_item_location", columnList = "location")})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoundItem extends GeoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "custody_location", length = 50)
  private String custodyLocation;

  private LocalDateTime foundAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private FoundItemStatusType statusType;

  public static FoundItem of(
      User user,
      Category category,
      Set<ColorCode> colorCodes,
      FoundItemRegisterDto registerDto,
      String imageUrl) {
    return FoundItem.builder()
        .user(user)
        .category(category)
        .colorCodes(colorCodes)
        .name(registerDto.itemName())
        .description(registerDto.description())
        .custodyLocation(registerDto.custodyLocation())
        .location(registerDto.location())
        .foundAt(LocalDateTime.now())
        .statusType(FoundItemStatusType.REGISTERED)
        .imageUrl(imageUrl)
        .build();
  }
}
