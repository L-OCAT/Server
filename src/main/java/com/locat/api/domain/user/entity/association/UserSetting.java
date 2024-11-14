package com.locat.api.domain.user.entity.association;

import com.locat.api.domain.common.entity.SecuredBaseEntity;
import com.locat.api.domain.setting.AppSetting;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "user_setting",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_setting",
          columnNames = {"user_id", "setting_id"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSetting extends SecuredBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "int UNSIGNED")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "setting_id", nullable = false, columnDefinition = "int UNSIGNED")
  private AppSetting appSetting;

  @NotNull @Lob
  @Column(name = "value", nullable = false)
  private String value;

  public static UserSetting ofDefault(User user, AppSetting appSetting) {
    return UserSetting.builder()
        .user(user)
        .appSetting(appSetting)
        .value(appSetting.getDefaultValue())
        .build();
  }
}
