package com.locat.api.domain.geo.found.entity;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(
    name = "found_item",
    indexes = {
      @Index(name = "idx_found_item_location", columnList = "location"),
      @Index(name = "idx_found_item_category", columnList = "category_id")
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoundItem extends GeoItem {

  @Serial private static final long serialVersionUID = 2024111401L;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "found_item_color_code",
      joinColumns = @JoinColumn(name = "item_id"),
      inverseJoinColumns = @JoinColumn(name = "color_id"))
  private Set<ColorCode> colorCodes = new HashSet<>();

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

  @Override
  public boolean isMatchable() {
    return !this.category.isOthers() || this.colorCodes.stream().noneMatch(ColorCode::isOthers);
  }

  @Override
  public Set<String> getColorNames() {
    return this.colorCodes.stream().map(ColorCode::getName).collect(Collectors.toSet());
  }
}
