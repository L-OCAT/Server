package com.locat.api.domain.geo.base.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "contact_center")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactCenter extends SecuredBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Size(max = 100)
  @NotNull @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Size(max = 100)
  @NotNull @Column(name = "contact", nullable = false, length = 100)
  private String contact;

  @Size(max = 255)
  @NotNull @Column(name = "address", nullable = false)
  private String address;
}
