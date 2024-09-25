package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "category")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity implements Comparable<Category> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 80)
  @NotNull @Column(name = "name", nullable = false, length = 80)
  private String name;

  @Column(name = "parent_id", columnDefinition = "int UNSIGNED")
  private Long parentId;

  @Override
  public int compareTo(Category other) {
    return this.id.compareTo(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.parentId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Category category = (Category) obj;
    return Objects.equals(this.id, category.id)
        && Objects.equals(this.name, category.name)
        && Objects.equals(this.parentId, category.parentId);
  }
}
