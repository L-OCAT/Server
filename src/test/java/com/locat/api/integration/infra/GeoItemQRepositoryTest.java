package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.dto.internal.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.dto.internal.LostItemSearchDto;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.infra.persistence.geo.GeoItemQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.GeoPage;

class GeoItemQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private GeoItemQRepository<LostItem> lostItemRepository;
  @Autowired private GeoItemQRepository<FoundItem> foundItemRepository;

  @Test
  @DisplayName("주어진 조건에 따라 분실물을 조회한다.")
  void findAllLostItemsByCriteria() {
    // Given
    var userId = 4L;
    var searchCriteria =
        LostItemSearchDto.fromRequest(false, GeoUtils.toPoint(37.572165, 127.016735), 50.0);
    var pageable = PageRequest.of(0, 10);

    // When
    GeoPage<LostItem> geoResults =
        this.lostItemRepository.findAllByCriteria(userId, searchCriteria, pageable);

    // Then
    assertThat(geoResults)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var lostItem = result.getContent();
              assertThat(lostItem).isNotNull().isExactlyInstanceOf(LostItem.class);
              assertThat(lostItem.getId()).isNotNull();
              assertThat(lostItem.getName()).isNotNull();
            });
  }

  @Test
  @DisplayName("주어진 조건에 따라 습득물을 조회한다.")
  void findAllFoundItemsByCriteria() {
    // Given
    var userId = 4L;
    var searchCriteria =
        FoundItemSearchDto.fromRequest(false, GeoUtils.toPoint(37.507339, 127.053768), 50.0);
    var pageable = PageRequest.of(0, 10);

    // When
    GeoPage<FoundItem> geoResults =
        this.foundItemRepository.findAllByCriteria(userId, searchCriteria, pageable);

    // Then
    assertThat(geoResults)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var foundItem = result.getContent();
              assertThat(foundItem).isNotNull().isExactlyInstanceOf(FoundItem.class);
              assertThat(foundItem.getId()).isNotNull();
              assertThat(foundItem.getName()).isNotNull();
            });
  }
}
