package com.locat.api.integration.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.locat.api.domain.geo.base.dto.internal.CategoryInfoDto;
import com.locat.api.infra.persistence.geo.base.CategoryQRepository;
import com.locat.api.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryQRepositoryTest extends AbstractIntegrationTest {

  @Autowired private CategoryQRepository repository;

  @Test
  @DisplayName("주어진 카테고리 ID에 해당하는 카테고리 정보를 조회할 수 있다.")
  void testFindInfoById() {
    // Given
    var categoryId = 37L; // 체크/신용카드

    // When
    var result = this.repository.findInfoById(categoryId);

    // Then
    assertThat(result)
        .isPresent()
        .get()
        .satisfies(
            dto -> {
              assertThat(dto.categoryId()).isEqualTo(categoryId);
              assertThat(dto.categoryName()).isNotNull();
              assertThat(dto.parentCategoryId()).isNotNull();
              assertThat(dto.parentCategoryName()).isEqualTo("귀금속");
              assertThat(dto.toCategoryPath()).isEqualTo("귀금속 > 체크/신용카드");
            });
  }

  @Test
  @DisplayName("모든 카테고리 정보를 조회할 수 있다.")
  void testFindAll() {
    // Given
    // no given

    // When
    var result = this.repository.findAll();

    // Then
    assertThat(result)
        .isNotNull()
        .isNotEmpty()
        .hasOnlyElementsOfType(CategoryInfoDto.class)
        .allSatisfy(
            dto -> {
              assertThat(dto.categoryId()).isNotNull();
              assertThat(dto.categoryName()).isNotNull();
            });
  }
}
