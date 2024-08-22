package com.locat.api.domain.geo.found.entity;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "found_item")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoundItem extends GeoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "custody_location", length = 50)
  private String custodyLocation;

  private ZonedDateTime foundAt;

  public static FoundItem of(
      User user, Category category, FoundItemRegisterDto registerDto, String imageUrl) {
    return FoundItem.builder()
        .user(user)
        .category(category)
        .categoryName(category.isCustom() ? registerDto.categoryName() : category.getName())
        .colorType(registerDto.colorType())
        .itemName(registerDto.itemName())
        .description(registerDto.description())
        .custodyLocation(registerDto.custodyLocation())
        .location(registerDto.location())
        .foundAt(ZonedDateTime.now())
        .imageUrl(imageUrl)
        .build();
  }
}
