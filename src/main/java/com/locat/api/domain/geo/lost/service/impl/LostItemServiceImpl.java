package com.locat.api.domain.geo.lost.service.impl;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.base.service.ColorCodeService;
import com.locat.api.domain.geo.lost.dto.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.LostItemSearchDto;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.geo.lost.service.LostItemService;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.domain.user.service.EndUserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.file.FileService;
import com.locat.api.infrastructure.repository.geo.GeoItemQRepository;
import com.locat.api.infrastructure.repository.geo.lost.LostItemRepository;
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
public class LostItemServiceImpl implements LostItemService {

  private static final String LOST_ITEM_IMAGE_DIRECTORY = "items/losts";

  private final LostItemRepository lostItemRepository;
  private final GeoItemQRepository<LostItem> lostItemQRepository;
  private final EndUserService endUserService;
  private final CategoryService categoryService;
  private final ColorCodeService colorCodeService;
  private final FileService fileService;

  @Override
  @Transactional(readOnly = true)
  public LostItem findById(Long id) {
    return this.lostItemRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_ITEM_LOST));
  }

  @Override
  @Transactional(readOnly = true)
  public GeoPage<LostItem> findAllByCondition(
      Long userId, LostItemSearchDto searchDto, Pageable pageable) {
    return this.lostItemQRepository.findByCondition(userId, searchDto, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LostItem> findTop10ByEndUser(EndUser user) {
    return this.lostItemRepository.findTop10ByUserOrderByCreatedAtDesc(user);
  }

  @Override
  public Long register(Long userId, LostItemRegisterDto registerDto, MultipartFile lostItemImage) {
    final EndUser user =
        this.endUserService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    final Category category = this.fetchCategoryById(registerDto.categoryId());
    final Set<ColorCode> colorCodes =
        registerDto.colorIds().stream().map(this::fetchColorCodeById).collect(Collectors.toSet());
    String imageUrl = null;

    if (lostItemImage != null) {
      imageUrl = this.fileService.upload(LOST_ITEM_IMAGE_DIRECTORY, lostItemImage);
    }
    return this.lostItemRepository
        .save(LostItem.of(user, category, colorCodes, registerDto, imageUrl))
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
