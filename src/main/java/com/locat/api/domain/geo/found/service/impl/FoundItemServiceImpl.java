package com.locat.api.domain.geo.found.service.impl;

import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.service.CategoryService;
import com.locat.api.domain.geo.found.dto.FoundItemRegisterDto;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.found.service.FoundItemService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.file.FileService;
import com.locat.api.infrastructure.repository.geo.GeoItemQRepository;
import com.locat.api.infrastructure.repository.geo.found.FoundItemRepository;
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
  private final FileService fileService;

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
  public Long register(
      Long userId, FoundItemRegisterDto registerDto, MultipartFile foundItemImage) {
    User user = this.userService.findById(userId);
    final Category category =
        this.categoryService.findById(registerDto.categoryId()).orElse(Category.ofCustom());

    final String imageUrl = this.fileService.upload(FOUND_ITEM_IMAGE_DIRECTORY, foundItemImage);
    return this.foundItemRepository
        .save(FoundItem.of(user, category, registerDto, imageUrl))
        .getId();
  }
}
