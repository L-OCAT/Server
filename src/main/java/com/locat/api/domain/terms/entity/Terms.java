package com.locat.api.domain.terms.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "terms")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms extends SecuredBaseEntity {
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

  @Size(max = 30)
  @NotNull @Column(name = "version", nullable = false, length = 30)
  private String version;
}
