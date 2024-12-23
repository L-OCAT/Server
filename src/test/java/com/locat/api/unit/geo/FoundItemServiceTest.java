package com.locat.api.unit.geo;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.event.GeoItemCreatedEvent;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.base.service.ColorCodeService;
import com.locat.api.domain.geo.found.dto.internal.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.internal.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.impl.FoundItemServiceImpl;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.infra.aws.s3.LocatS3Client;
import com.locat.api.infra.persistence.geo.GeoItemQRepository;
import com.locat.api.infra.persistence.geo.found.FoundItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

final class FoundItemServiceTest {

  private static final String FOUND_ITEM_IMAGE_DIRECTORY = "items/founds";

  @InjectMocks private FoundItemServiceImpl service;
  @Mock private FoundItemRepository foundItemRepository;
  @Mock private GeoItemQRepository<FoundItem> foundItemQRepository;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private UserService userService;
  @Mock private CategoryService categoryService;
  @Mock private ColorCodeService colorCodeService;
  @Mock private LocatS3Client s3Client;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("습득물 조회에 성공한다.")
  void testFindById() {
    // Given
    final long id = 1L;
    given(this.foundItemRepository.findById(id)).willReturn(Optional.of(mock(FoundItem.class)));

    // When & Then
    assertThatCode(() -> this.service.findById(id)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("주어진 ID를 가지는 습득물이 없으면, NoSuchEntityException을 던진다.")
  void testFindByIdWhenNotFound() {
    // Given
    final long id = 1L;
    given(this.foundItemRepository.findById(id)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.findById(id))
        .isExactlyInstanceOf(NoSuchEntityException.class);
  }

  @Test
  @DisplayName("조건에 맞는 습득물 목록을 조회한다.")
  void testFindAllByCondition() {
    // Given
    final long userId = 1L;
    final FoundItemSearchDto searchDto = mock(FoundItemSearchDto.class);
    final Pageable pageable = Pageable.ofSize(10);

    // When & Then
    assertThatCode(() -> this.service.findAllByCondition(userId, searchDto, pageable))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("주어진 사용자가 등록한 최근 10개의 습득물을 조회한다.")
  void testFindTop10ByEndUser() {
    // Given
    final User mockUser = mock(User.class);
    given(this.foundItemRepository.findTop10ByUserOrderByCreatedAtDesc(mockUser))
        .willReturn(List.of());

    // When & Then
    assertThatCode(() -> this.service.findTop10ByEndUser(mockUser)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("모든 조건을 충족하면, 습득물을 등록한다.")
  void testRegister() {
    // Given
    Long userId = 1L;
    Long categoryId = 2L;
    Set<Long> colorIds = Set.of(3L, 4L);
    FoundItemRegisterDto registerDto =
        new FoundItemRegisterDto(
            categoryId, colorIds, "item name", "item desc", "custody location", null);
    MultipartFile mockImage = mock(MultipartFile.class);
    String uploadedImageUrl = "https://example.com/image.jpg";

    User mockUser = mock(User.class);
    Category mockCategory = mock(Category.class);
    ColorCode colorCode1 = mock(ColorCode.class);
    ColorCode colorCode2 = mock(ColorCode.class);
    FoundItem savedFoundItem = mock(FoundItem.class);

    given(this.userService.findById(userId)).willReturn(Optional.of(mockUser));
    given(this.categoryService.findById(categoryId)).willReturn(Optional.of(mockCategory));
    given(this.colorCodeService.findById(3L)).willReturn(Optional.of(colorCode1));
    given(this.colorCodeService.findById(4L)).willReturn(Optional.of(colorCode2));
    given(this.s3Client.upload(FOUND_ITEM_IMAGE_DIRECTORY, mockImage)).willReturn(uploadedImageUrl);
    given(this.foundItemRepository.save(any(FoundItem.class))).willReturn(savedFoundItem);
    given(savedFoundItem.getId()).willReturn(10L);

    // When
    Long result = this.service.register(userId, registerDto, mockImage);

    // Then
    assertThat(result).isEqualTo(10L);
    then(this.userService).should().findById(userId);
    then(this.categoryService).should().findById(categoryId);
    then(this.colorCodeService).should(times(2)).findById(anyLong());
    then(this.s3Client).should().upload(eq(FOUND_ITEM_IMAGE_DIRECTORY), eq(mockImage));
    then(this.foundItemRepository).should().save(any(FoundItem.class));
    then(this.eventPublisher).should().publishEvent(any(GeoItemCreatedEvent.class));
  }

  @Test
  @DisplayName("사용자가 존재하지 않으면 NoSuchEntityException을 던진다.")
  void shouldThrowWhenNoUser() {
    // Given
    Long userId = 1L;
    FoundItemRegisterDto registerDto = mock(FoundItemRegisterDto.class);
    MultipartFile mockImage = mock(MultipartFile.class);

    given(this.userService.findById(userId)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> this.service.register(userId, registerDto, mockImage))
        .isExactlyInstanceOf(NoSuchEntityException.class)
        .hasMessageContaining(ApiExceptionType.NOT_FOUND_USER.getMessage());
    then(this.userService).should().findById(userId);
  }

  @Test
  @DisplayName("카테고리가 존재하지 않으면 NoSuchEntityException을 던진다.")
  void shouldThrowWhenNoCategory() {
    // Given
    Long userId = 1L;
    Long categoryId = 2L;
    Set<Long> colorIds = Set.of(3L, 4L);
    FoundItemRegisterDto registerDto =
        new FoundItemRegisterDto(
            categoryId, colorIds, "item name", "item desc", "custody location", null);
    MultipartFile mockImage = mock(MultipartFile.class);

    User mockUser = mock(User.class);
    given(this.userService.findById(userId)).willReturn(Optional.of(mockUser));
    given(this.categoryService.findById(categoryId)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> this.service.register(userId, registerDto, mockImage))
        .isExactlyInstanceOf(NoSuchEntityException.class)
        .hasMessageContaining(ApiExceptionType.NOT_FOUND_CATEGORY.getMessage());
    then(this.userService).should().findById(userId);
    then(this.categoryService).should().findById(categoryId);
  }

  @Test
  @DisplayName("색상 코드가 존재하지 않으면 NoSuchEntityException을 던진다.")
  void shouldThrowWhenNoColors() {
    // Given
    Long userId = 1L;
    Long categoryId = 2L;
    Set<Long> colorIds = Set.of(3L);
    FoundItemRegisterDto registerDto =
        new FoundItemRegisterDto(
            categoryId, colorIds, "item name", "item desc", "custody location", null);
    MultipartFile mockImage = mock(MultipartFile.class);

    User mockUser = mock(User.class);
    Category mockCategory = mock(Category.class);
    given(this.userService.findById(userId)).willReturn(Optional.of(mockUser));
    given(this.categoryService.findById(categoryId)).willReturn(Optional.of(mockCategory));
    given(this.colorCodeService.findById(3L)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> this.service.register(userId, registerDto, mockImage))
        .isExactlyInstanceOf(NoSuchEntityException.class)
        .hasMessageContaining(ApiExceptionType.NOT_FOUND_COLOR_CODE.getMessage());
    then(this.userService).should().findById(userId);
    then(this.categoryService).should().findById(categoryId);
    then(this.colorCodeService).should().findById(3L);
  }

  @Test
  @DisplayName("이미지가 없으면, 이미지 URL 없이 습득물을 등록한다.")
  void shouldRegisterWithoutImage() {
    // Given
    Long userId = 1L;
    Long categoryId = 2L;
    Set<Long> colorIds = Set.of(3L, 4L);
    FoundItemRegisterDto registerDto =
        new FoundItemRegisterDto(
            categoryId, colorIds, "item name", "item desc", "custody location", null);

    User mockUser = mock(User.class);
    Category mockCategory = mock(Category.class);
    ColorCode colorCode1 = mock(ColorCode.class);
    ColorCode colorCode2 = mock(ColorCode.class);
    FoundItem savedFoundItem = mock(FoundItem.class);

    given(this.userService.findById(userId)).willReturn(Optional.of(mockUser));
    given(this.categoryService.findById(categoryId)).willReturn(Optional.of(mockCategory));
    given(this.colorCodeService.findById(3L)).willReturn(Optional.of(colorCode1));
    given(this.colorCodeService.findById(4L)).willReturn(Optional.of(colorCode2));
    given(this.foundItemRepository.save(any(FoundItem.class))).willReturn(savedFoundItem);
    given(savedFoundItem.getId()).willReturn(10L);

    // When
    Long result = this.service.register(userId, registerDto, null);

    // Then
    assertThat(result).isEqualTo(10L);
    then(this.userService).should().findById(userId);
    then(this.categoryService).should().findById(categoryId);
    then(this.colorCodeService).should().findById(3L);
    then(this.colorCodeService).should().findById(4L);
    then(this.foundItemRepository).should().save(any(FoundItem.class));
    then(this.eventPublisher).should().publishEvent(any(GeoItemCreatedEvent.class));
  }
}
