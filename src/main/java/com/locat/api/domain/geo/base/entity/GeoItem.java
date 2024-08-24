package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.geo.Point;

/**
 * 분실물 & 습득물 공통 Entity<br>
 * {@link SecuredBaseEntity}를 상속받고, {@link FoundItem}과 {@link LostItem}의 공통 속성을 정의합니다.
 */
@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GeoItem extends SecuredBaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      updatable = false,
      columnDefinition = "int UNSIGNED")
  protected User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  protected Category category;

  @Column(name = "category_name", length = 50)
  protected String categoryName;

  protected ColorType colorType;

  @Column(name = "item_name", length = 50)
  protected String itemName;

  @Column(name = "description", length = 500)
  protected String description;

  @Column(name = "location", columnDefinition = "POINT SRID 4326")
  protected Point location;

  protected String imageUrl;

  public String getCategoryName() {
    return this.category.isCustom() ? this.categoryName : this.category.getName();
  }
}