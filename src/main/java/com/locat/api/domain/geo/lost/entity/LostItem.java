package com.locat.api.domain.geo.lost.entity;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.entity.GeoItem;
import com.locat.api.domain.geo.lost.dto.LostItemRegisterDto;
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
    name = "lost_item",
    indexes = {@Index(name = "idx_lost_item_location", columnList = "location")})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostItem extends GeoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

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
}
