package com.locat.api.domain.setting;

import com.locat.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "app_setting",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_name",
          columnNames = {"name"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppSetting extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 100)
  @NotNull @Column(name = "name", nullable = false, length = 100)
  private String name;

  @NotNull @Lob
  @Column(name = "default_value", nullable = false)
  private String defaultValue;
}
