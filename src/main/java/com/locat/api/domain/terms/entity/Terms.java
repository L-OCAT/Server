package com.locat.api.domain.terms.entity;

import com.locat.api.domain.core.BaseEntity;
import com.locat.api.domain.terms.dto.TermsRegisterDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "terms",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_type_version",
          columnNames = {"type", "version"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private TermsType type;

  @Size(max = 150)
  @NotNull @Column(name = "title", nullable = false, length = 150)
  private String title;

  @NotNull @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @NotNull @Digits(integer = 2, fraction = 1)
  @Column(name = "version", nullable = false, precision = 3, scale = 1)
  private BigDecimal version;

  public static Terms from(TermsRegisterDto registerDto, BigDecimal version) {
    return Terms.builder()
        .type(registerDto.type())
        .title(registerDto.title())
        .content(registerDto.content())
        .version(version)
        .build();
  }
}
