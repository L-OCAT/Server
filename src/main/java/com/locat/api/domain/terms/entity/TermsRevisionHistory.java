package com.locat.api.domain.terms.entity;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "terms_revision_history")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsRevisionHistory extends SecuredBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private TermsType type;

  @Column(name = "is_required", nullable = false)
  private boolean isRequired;

  @Size(max = 150)
  @NotNull @Column(name = "title", nullable = false, length = 150)
  private String title;

  @NotNull @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "version", nullable = false)
  private Double version;

  @Column(name = "revision_note", nullable = false, columnDefinition = "TEXT")
  private String revisionNote;

  public static TermsRevisionHistory of(Terms terms, String revisionNote) {
    return TermsRevisionHistory.builder()
        .type(terms.getType())
        .isRequired(terms.isRequired())
        .title(terms.getTitle())
        .content(terms.getContent())
        .version(terms.getVersion())
        .revisionNote(revisionNote)
        .build();
  }
}
