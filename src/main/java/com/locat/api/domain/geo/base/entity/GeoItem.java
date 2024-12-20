package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      updatable = false,
      columnDefinition = "int UNSIGNED",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  protected User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  protected Category category;

  @Column(name = "name", nullable = false, length = 50)
  protected String name;

  @Column(name = "description", length = 500)
  protected String description;

  @Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
  protected Point location;

  @Column(name = "image_url")
  protected String imageUrl;

  public Long getCategoryId() {
    return this.category.getId();
  }

  protected abstract boolean isMatchable();

  protected abstract Set<String> getColorNames();
}
