package com.locat.api.domain.user.entity.association;

import com.locat.api.domain.common.entity.BaseEntity;
import com.locat.api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "admin_device_id",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_admin_device_id",
          columnNames = {"admin_user_id", "device_id"})
    })
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDeviceId extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "admin_user_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User adminUser;

  @Column(name = "device_id", nullable = false)
  private String deviceId;

  @Column(name = "is_trusted", nullable = false)
  private boolean isTrusted;

  public static AdminDeviceId of(User user, String deviceId, boolean isTrusted) {
    return AdminDeviceId.builder().adminUser(user).deviceId(deviceId).isTrusted(isTrusted).build();
  }
}
