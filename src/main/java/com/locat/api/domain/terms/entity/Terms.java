package com.locat.api.domain.terms.entity;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.terms.dto.TermsUpsertDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
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

  @Transient private static final double INITIAL_VERSION = 1.0;
  @Transient private static final double VERSION_INCREMENT = 0.1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private TermsType type;

  @Column(name = "is_required", nullable = false)
  private boolean isRequired;

  @Size(max = 150)
  @NotNull @Column(name = "title", nullable = false, length = 150)
  private String title;

  @NotNull @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @NotNull @Column(name = "version", nullable = false)
  private Double version;

  public static Terms create(TermsUpsertDto upsertDto) {
    return Terms.builder()
        .type(upsertDto.type())
        .isRequired(upsertDto.isRequired())
        .title(upsertDto.title())
        .content(upsertDto.content())
        .version(INITIAL_VERSION)
        .build();
  }

  public Terms update(TermsUpsertDto upsertDto) {
    return this.toBuilder()
        .isRequired(upsertDto.isRequired())
        .title(upsertDto.title())
        .content(upsertDto.content())
        .version(this.version + VERSION_INCREMENT)
        .build();
  }
}
