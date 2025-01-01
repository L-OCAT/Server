package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.admin.dto.internal.AdminItemStatByCategoryDto;
import com.locat.api.domain.admin.dto.internal.MonthlyGeoItemStatistics;
import com.locat.api.infra.persistence.admin.AdminDashboardQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AdminDashboardQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private AdminDashboardQRepository repository;

  @Test
  @DisplayName("대시보드 요약 정보(총 사용자, 총 분실물, 총 습득물)를 조회할 수 있다.")
  void getSummary() {
    // Given
    // no given

    // When
    var result = this.repository.getSummary();

    // Then
    assertThat(result)
        .isNotNull()
        .satisfies(
            summary -> {
              assertThat(summary.totalUsers()).isPositive();
              assertThat(summary.totalLostItems()).isPositive();
              assertThat(summary.totalFoundItems()).isPositive();
            });
  }

  @Test
  @DisplayName("카테고리별 분실물/습득물 통계 정보를 조회할 수 있다.")
  void getStatByCategory() {
    // Given
    // no given

    // When
    var result = this.repository.getStatByCategory();

    // Then
    assertThat(result)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminItemStatByCategoryDto.class)
        .allSatisfy(
            stat -> {
              assertThat(stat.categoryName()).isNotBlank();
              assertThat(stat.lostItemCount()).isNotNegative();
              assertThat(stat.foundItemCount()).isNotNegative();
            });
  }

  @Test
  @DisplayName("월별 분실물/습득물 통계 정보를 조회할 수 있다.")
  void getMonthlyItemStat() {
    // Given
    // no given

    // When
    var result = this.repository.getMonthlyItemStat();
    List<String> monthLabelsResult = result.monthLabels();
    List<MonthlyGeoItemStatistics> lostItemCountResult = result.lostItemCount();
    List<MonthlyGeoItemStatistics> foundItemCountResult = result.foundItemCount();

    // Then
    assertThat(result).isNotNull();
    assertThat(monthLabelsResult).allSatisfy(label -> assertThat(label).isNotBlank().endsWith("월"));
    assertThat(lostItemCountResult)
        .isNotEmpty()
        .hasSizeLessThan(monthLabelsResult.size())
        .allSatisfy(
            stat -> {
              assertThat(stat.month()).isNotBlank().endsWith("월");
              assertThat(stat.count()).isNotNegative();
            });
    assertThat(foundItemCountResult)
        .isNotEmpty()
        .hasSizeLessThan(monthLabelsResult.size())
        .allSatisfy(
            stat -> {
              assertThat(stat.month()).isNotBlank().endsWith("월");
              assertThat(stat.count()).isNotNegative();
            });
  }
}
