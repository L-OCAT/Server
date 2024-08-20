package com.locat.api.domain.geo.found;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserService;
import com.locat.api.global.exception.ApiExceptionType;
import com.locat.api.global.exception.NoSuchEntityException;
import com.locat.api.global.file.FileService;
import com.locat.api.infrastructure.aws.dynamodb.FoundItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FoundItemServiceImpl implements FoundItemService {

  private static final String FOUND_ITEM_IMAGE_DIRECTORY = "item/founds";

  private final FoundItemRepository foundItemRepository;
  private final UserService userService;
  private final FileService fileService;

  @Override
  @Transactional(readOnly = true)
  public FoundItem findById(Long id) {
    return this.foundItemRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_ITEM_FOUND));
  }

  @Override
  public Page<FoundItem> findAllByCondition(FoundItemSearchDto searchDto, Pageable pageable) {
    final long totalCount = this.foundItemRepository.countAll();
    this.foundItemRepository.findByLocationNear(searchDto.location(), searchDto.distance());
    return new PageImpl<>(this.foundItemRepository.findAll(), pageable, totalCount);
  }

  @Override
  public Long register(
      Long userId, FoundItemRegisterRequest request, MultipartFile foundItemImage) {
    User user = this.userService.findById(userId);

    final String imageUrl = this.fileService.upload(FOUND_ITEM_IMAGE_DIRECTORY, foundItemImage);
    return this.foundItemRepository.save(FoundItem.of(user, request, imageUrl)).getId();
  }
}
