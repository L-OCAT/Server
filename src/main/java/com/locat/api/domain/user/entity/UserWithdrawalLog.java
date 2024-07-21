package com.locat.api.domain.user.entity;

import com.locat.api.domain.core.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "user_withdrawal_log")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithdrawalLog extends SecuredBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "user_id", columnDefinition = "int UNSIGNED not null")
  private Long userId;

  @NotNull @Lob
  @Column(name = "reason", nullable = false)
  private String reason;
}
