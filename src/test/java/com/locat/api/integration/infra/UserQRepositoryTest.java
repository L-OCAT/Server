package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.user.dto.criteria.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.internal.UserInfoDto;
import com.locat.api.infra.persistence.user.UserQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

class UserQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private UserQRepository repository;

  @Test
  @DisplayName("주어진 조건에 맞는 사용자 목록을 조회할 수 있다.")
  @Transactional(readOnly = true)
  void testFindAllByCriteria() {
    // Given
    var pageable = PageRequest.of(0, 10);
    var criteria1 = AdminUserSearchCriteria.of(null, null, null, null);
    var criteria2 = AdminUserSearchCriteria.of("LOCAT", null, null, null);
    var criteria3 = AdminUserSearchCriteria.of(null, "team.l0oocat@gmail.com", null, null);
    var criteria4 =
        AdminUserSearchCriteria.of(
            null, null, LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 31));

    // When
    var result1 = this.repository.findAllByCriteria(criteria1, pageable);
    var result2 = this.repository.findAllByCriteria(criteria2, pageable);
    var result3 = this.repository.findAllByCriteria(criteria3, pageable);
    var result4 = this.repository.findAllByCriteria(criteria4, pageable);

    // Then
    assertThat(result1).isNotNull().isNotEmpty();
    assertThat(result2)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(UserInfoDto.class)
        .allSatisfy(dto -> assertThat(dto.nickname()).contains("LOCAT"));
    assertThat(result3)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(UserInfoDto.class)
        .allSatisfy(dto -> assertThat(dto.email()).isEqualTo("team.l0oocat@gmail.com"));
    assertThat(result4)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(UserInfoDto.class)
        .allSatisfy(
            dto ->
                assertThat(dto.createdAt())
                    .isBetween(
                        LocalDate.of(2024, 10, 1).atStartOfDay(),
                        LocalDate.of(2024, 10, 31).atTime(LocalTime.MAX)));
  }
}
