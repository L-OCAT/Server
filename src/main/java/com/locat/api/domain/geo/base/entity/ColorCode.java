package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "color_code")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorCode extends BaseEntity {

  @Serial private static final long serialVersionUID = 2024100901L;

  private static final String NAME_OTHERS = "기타";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "hex_code", nullable = false, length = 7)
  private String hexCode;

  @Column(name = "name", nullable = false, length = 80)
  private String name;

  public static ColorCode of(String hexCode, String name) {
    return ColorCode.builder().hexCode(hexCode).name(name).build();
  }

  public boolean isOthers() {
    return NAME_OTHERS.equals(this.name);
  }
}
