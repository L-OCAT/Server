package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.core.BaseEntity;
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
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 80)
  @NotNull @Column(name = "name", nullable = false, length = 80)
  private String name;

  @Column(name = "parent_id", columnDefinition = "int UNSIGNED")
  private Long parentId;
}
