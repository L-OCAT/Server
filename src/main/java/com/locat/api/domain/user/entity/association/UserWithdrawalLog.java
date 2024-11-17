package com.locat.api.domain.user.entity.association;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "user_withdrawal_log")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithdrawalLog extends SecuredBaseEntity {

  @Serial private static final long serialVersionUID = 2024091101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @Column(name = "user_id", columnDefinition = "int UNSIGNED not null")
  private Long userId;

  @NotNull @Lob
  @Column(name = "reason", nullable = false)
  private String reason;

  public static UserWithdrawalLog of(final Long userId, final String reason) {
    return UserWithdrawalLog.builder().userId(userId).reason(reason).build();
  }
}
