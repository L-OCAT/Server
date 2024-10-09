package com.locat.api.domain.geo.lost.entity;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.lost.dto.LostItemRegisterDto;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
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
    name = "lost_item",
    indexes = {
      @Index(name = "idx_lost_item_location", columnList = "location"),
      @Index(name = "idx_lost_item_category", columnList = "category_id")
    })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostItem extends GeoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "lost_item_color_code",
      joinColumns = @JoinColumn(name = "item_id"),
      inverseJoinColumns = @JoinColumn(name = "color_id"))
  private Set<ColorCode> colorCodes = new HashSet<>();

  @Column(name = "is_willing_to_pay_gratuity")
  private Boolean isWillingToPayGratuity;

  @Column(name = "gratuity")
  private Integer gratuity;

  private LocalDateTime lostAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private LostItemStatusType statusType;

  public static LostItem of(
      User user,
      Category category,
      Set<ColorCode> colorCodes,
      LostItemRegisterDto registerDto,
      String imageUrl) {
    return LostItem.builder()
        .user(user)
        .category(category)
        .colorCodes(colorCodes)
        .name(registerDto.itemName())
        .description(registerDto.description())
        .isWillingToPayGratuity(registerDto.isWillingToPayGratuity())
        .gratuity(registerDto.gratuity())
        .location(registerDto.location())
        .lostAt(LocalDateTime.now())
        .statusType(LostItemStatusType.REGISTERED)
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
