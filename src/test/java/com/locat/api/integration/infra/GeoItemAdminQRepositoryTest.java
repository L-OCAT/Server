package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemAdminSearchCriteria;
import com.locat.api.domain.geo.base.dto.internal.AdminGeoItemSearchQueryResult;
import com.locat.api.domain.geo.base.entity.GeoItemType;
import com.locat.api.infra.persistence.geo.GeoItemAdminQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class GeoItemAdminQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private GeoItemAdminQRepository repository;

  @Test
  @DisplayName("주어진 조건에 따라 분실물 & 습득물(관리자용 데이터)을 조회할 수 있다.")
  void testFindAllByAdminCriteria() {
    // Given
    var pageable = PageRequest.of(0, 10);
    var criteria1 = GeoItemAdminSearchCriteria.of(null, null, null, null, null, null, null, null);
    var criteria2 =
        GeoItemAdminSearchCriteria.of(
            null,
            null,
            null,
            null,
            null,
            null,
            LocalDate.of(2024, 12, 1),
            LocalDate.of(2024, 12, 31));
    var criteria3 =
        GeoItemAdminSearchCriteria.of(
            GeoItemType.FOUND.name(), null, null, null, null, null, null, null);
    var criteria4 =
        GeoItemAdminSearchCriteria.of(
            GeoItemType.FOUND.name(), "에어팟", "서울", "강남구", null, null, null, null);
    var criteria5 =
        GeoItemAdminSearchCriteria.of(
            GeoItemType.LOST.name(), null, null, null, null, null, null, null);
    var criteria6 =
        GeoItemAdminSearchCriteria.of(
            GeoItemType.LOST.name(), "기후동행", "서울", "종로구", null, null, null, null);

    // When
    var result1 = this.repository.findAllByAdminCriteria(criteria1, pageable);
    var result2 = this.repository.findAllByAdminCriteria(criteria2, pageable);
    var result3 = this.repository.findAllByAdminCriteria(criteria3, pageable);
    var result4 = this.repository.findAllByAdminCriteria(criteria4, pageable);
    var result5 = this.repository.findAllByAdminCriteria(criteria5, pageable);
    var result6 = this.repository.findAllByAdminCriteria(criteria6, pageable);

    // Then
    assertThat(result1)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class);
    assertThat(result2)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class)
        .allSatisfy(
            dto ->
                assertThat(dto.createdAt())
                    .isBetween(
                        LocalDate.of(2024, 12, 1).atStartOfDay(),
                        LocalDate.of(2024, 12, 31).atTime(LocalTime.MAX)));
    assertThat(result3)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class)
        .allSatisfy(dto -> assertThat(dto.geoItemType()).isEqualTo(GeoItemType.FOUND));
    assertThat(result4)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class)
        .allSatisfy(
            dto -> {
              assertThat(dto.region1()).startsWith("서울");
              assertThat(dto.region2()).isEqualTo("강남구");
            });
    assertThat(result5)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class)
        .allSatisfy(dto -> assertThat(dto.geoItemType()).isEqualTo(GeoItemType.LOST));
    assertThat(result6)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(AdminGeoItemSearchQueryResult.class)
        .allSatisfy(
            dto -> {
              assertThat(dto.region1()).startsWith("서울");
              assertThat(dto.region2()).isEqualTo("종로구");
            });
  }
}
