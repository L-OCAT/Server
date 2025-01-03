package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.admin.dto.internal.AdminUserStatDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.infra.persistence.admin.UserStatisticQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserStatisticQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private UserStatisticQRepository repository;

  @Test
  @DisplayName("사용자의 활동 통계 정보를 조회할 수 있다.")
  void testGetUserStat() {
    // Given
    var user = User.builder().id(4L).build();

    // When
    var result = this.repository.getUserStat(user);

    // Then
    assertThat(result)
        .isNotNull()
        .isExactlyInstanceOf(AdminUserStatDto.class)
        .satisfies(
            userStat -> {
              assertThat(userStat.id()).isEqualTo(4L);
              assertThat(userStat.type()).isNotNull();
              assertThat(userStat.oAuthType()).isNotNull();
              assertThat(userStat.email()).isNotNull();
              assertThat(userStat.nickname()).isNotNull();
              assertThat(userStat.statusType()).isNotNull();
              assertThat(userStat.createdAt()).isNotNull();
              assertThat(userStat.updatedAt()).isNotNull();
              assertThat(userStat.agreementDetails())
                  .isNotNull()
                  .isNotEmpty()
                  .allSatisfy(
                      agreementDetail -> assertThat(agreementDetail.termsName()).isNotNull());
              assertThat(userStat.activityDetails())
                  .isNotNull()
                  .satisfies(
                      activityDetails -> {
                        assertThat(activityDetails.totalRegisteredFoundItems()).isPositive();
                        assertThat(activityDetails.totalRegisteredLostItems()).isPositive();
                      });
            });
  }
}
