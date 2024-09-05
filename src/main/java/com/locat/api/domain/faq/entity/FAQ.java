package com.locat.api.domain.faq.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "faq")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FAQ extends SecuredBaseEntity {

  @Id
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private FaqType type;

  @Size(max = 100)
  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @Lob
  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;
}
