package com.locat.api.domain.geo.found.service.impl;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.base.service.ColorCodeService;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.infra.aws.s3.LocatS3Client;
import com.locat.api.infra.persistence.geo.GeoItemQRepository;
import com.locat.api.infra.persistence.geo.found.FoundItemRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FoundItemServiceImpl implements FoundItemService {

  private static final String FOUND_ITEM_IMAGE_DIRECTORY = "items/founds";

  private final FoundItemRepository foundItemRepository;
  private final GeoItemQRepository<FoundItem> foundItemQRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final ColorCodeService colorCodeService;
  private final LocatS3Client s3Client;

  @Override
  @Transactional(readOnly = true)
  public FoundItem findById(Long id) {
    return this.foundItemRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_ITEM_FOUND));
  }

  @Override
  @Transactional(readOnly = true)
  public GeoPage<FoundItem> findAllByCondition(
      Long userId, FoundItemSearchDto searchDto, Pageable pageable) {
    return this.foundItemQRepository.findByCondition(userId, searchDto, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FoundItem> findTop10ByEndUser(User user) {
    return this.foundItemRepository.findTop10ByUserOrderByCreatedAtDesc(user);
  }

  @Override
  public Long register(
      Long userId, FoundItemRegisterDto registerDto, MultipartFile foundItemImage) {
    User user =
        this.userService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    final Category category = this.fetchCategoryById(registerDto.categoryId());
    final Set<ColorCode> colorCodes =
        registerDto.colorIds().stream().map(this::fetchColorCodeById).collect(Collectors.toSet());

    String imageUrl = null;
    if (foundItemImage != null) {
      imageUrl = this.s3Client.upload(FOUND_ITEM_IMAGE_DIRECTORY, foundItemImage);
    }
    return this.foundItemRepository
        .save(FoundItem.of(user, category, colorCodes, registerDto, imageUrl))
        .getId();
  }

  private Category fetchCategoryById(final Long categoryId) {
    return this.categoryService
        .findById(categoryId)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_CATEGORY));
  }

  private ColorCode fetchColorCodeById(final Long colorId) {
    return this.colorCodeService
        .findById(colorId)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_COLOR_CODE));
  }
}
