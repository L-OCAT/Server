package com.locat.api.domain.user.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "user_setting")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSetting extends SecuredBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "int UNSIGNED")
  private User user;

  @Column(name = "setting_id", columnDefinition = "int UNSIGNED not null")
  private Long settingId;

  @NotNull @Lob
  @Column(name = "value", nullable = false)
  private String value;
}
