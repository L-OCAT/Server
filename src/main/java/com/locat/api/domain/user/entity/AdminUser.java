package com.locat.api.domain.user.entity;

import com.locat.api.domain.user.entity.association.AdminDeviceId;
import com.locat.api.domain.user.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "admin_user")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUser extends User {

  @Size(max = 60)
  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "is_password_expired", nullable = false)
  private boolean isPasswordExpired;

  @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AdminDeviceId> adminDeviceIds = new ArrayList<>();

  public void promote(int level, String tempPassword) {
    this.userType = UserType.fromLevel(level);
    this.password = tempPassword;
    this.isPasswordExpired = true;
  }

  public void resetPassword(String encodedPassword) {
    this.password = encodedPassword;
    this.isPasswordExpired = false;
  }

  public boolean isTrustedDevice(String deviceId) {
    return this.adminDeviceIds.stream()
        .anyMatch(device -> device.getDeviceId().equals(deviceId) && device.isTrusted());
  }

  public void addTrustedDevice(String deviceId, boolean isTrusted) {
    this.adminDeviceIds.add(AdminDeviceId.of(this, deviceId, isTrusted));
  }

  public void removeTrustedDevice(String deviceId) {
    this.adminDeviceIds.removeIf(device -> device.getDeviceId().equals(deviceId));
  }
}
