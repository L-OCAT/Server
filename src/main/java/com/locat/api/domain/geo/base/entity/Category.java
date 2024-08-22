package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "category")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends SecuredBaseEntity {

  public static final String CUSTOM_CATEGORY_NAME = "직접 입력";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 80)
  @NotNull @Column(name = "name", nullable = false, length = 80)
  private String name;

  @Column(name = "parent_id", columnDefinition = "int UNSIGNED")
  private Long parentId;

  public static Category ofCustom() {
    return Category.builder().id(1L).name(CUSTOM_CATEGORY_NAME).parentId(null).build();
  }

  public boolean isCustom() {
    return this.name.equals(CUSTOM_CATEGORY_NAME);
  }
}
