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
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoPage;

class GeoItemQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private GeoItemQRepository<LostItem> lostItemRepository;
  @Autowired private GeoItemQRepository<FoundItem> foundItemRepository;

  @Test
  @DisplayName("주어진 조건에 따라 분실물을 조회한다.")
  void findAllLostItemsByCriteria() {
    // Given
    var userId = 4L;
    var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lostAt"));
    var criteria1 =
        LostItemSearchDto.fromRequest(false, GeoUtils.toPoint(37.572165, 127.016735), 50.0);
    var criteria2 =
        LostItemSearchDto.fromRequest(true, GeoUtils.toPoint(37.572165, 127.016735), 50.0);

    // When
    GeoPage<LostItem> result1 =
        this.lostItemRepository.findAllByCriteria(userId, criteria1, pageable);
    GeoPage<LostItem> result2 =
        this.lostItemRepository.findAllByCriteria(userId, criteria2, pageable);

    // Then
    assertThat(result1)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var lostItem = result.getContent();
              assertThat(lostItem).isNotNull().isExactlyInstanceOf(LostItem.class);
              assertThat(lostItem.getId()).isNotNull();
              assertThat(lostItem.getName()).isNotNull();
            });
    assertThat(result2)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var lostItem = result.getContent();
              assertThat(lostItem).isNotNull().isExactlyInstanceOf(LostItem.class);
              assertThat(lostItem.getId()).isNotNull();
              assertThat(lostItem.getName()).isNotNull();
              assertThat(lostItem.getCreatedBy()).isEqualTo(userId);
            });
  }

  @Test
  @DisplayName("주어진 조건에 따라 습득물을 조회한다.")
  void findAllFoundItemsByCriteria() {
    // Given
    var userId = 4L;
    var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "foundAt"));
    var criteria1 =
        FoundItemSearchDto.fromRequest(false, GeoUtils.toPoint(37.507339, 127.053768), 50.0);
    var criteria2 =
        FoundItemSearchDto.fromRequest(true, GeoUtils.toPoint(37.507339, 127.053768), 50.0);

    // When
    GeoPage<FoundItem> result1 =
        this.foundItemRepository.findAllByCriteria(userId, criteria1, pageable);
    GeoPage<FoundItem> result2 =
        this.foundItemRepository.findAllByCriteria(userId, criteria2, pageable);

    // Then
    assertThat(result1)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var foundItem = result.getContent();
              assertThat(foundItem).isNotNull().isExactlyInstanceOf(FoundItem.class);
              assertThat(foundItem.getId()).isNotNull();
              assertThat(foundItem.getName()).isNotNull();
            });
    assertThat(result2)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              var foundItem = result.getContent();
              assertThat(foundItem).isNotNull().isExactlyInstanceOf(FoundItem.class);
              assertThat(foundItem.getId()).isNotNull();
              assertThat(foundItem.getName()).isNotNull();
              assertThat(foundItem.getCreatedBy()).isEqualTo(userId);
            });
  }
}
