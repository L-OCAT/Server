package com.locat.api.domain.geo.lost.service;

import com.locat.api.domain.geo.lost.dto.internal.LostItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.internal.LostItemSearchDto;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.infra.aws.exception.FileOperationException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.web.multipart.MultipartFile;

/** 분실물 도메인 관련 서비스 */
public interface LostItemService {

  /**
   * ID로 분실물 조회
   *
   * @param id 분실물 ID
   * @return 분실물
   * @throws NoSuchEntityException 분실물이 존재하지 않는 경우
   */
  LostItem findById(final Long id);

  /**
   * 조건에 맞는 분실물 목록 조회
   *
   * @param userId 사용자 ID
   * @param searchDto 분실물 검색 조건
   * @param pageable 페이지 요청 정보
   * @return 위치 정보를 포함하는 분실물 목록
   */
  GeoPage<LostItem> findAllByCondition(
      final Long userId, LostItemSearchDto searchDto, Pageable pageable);

  /**
   * 사용자가 가장 최근에 등록한 분실물 목록 조회
   *
   * @param user 조회 대상 사용자
   * @return 사용자가 등록한 분실물 목록 (최대 10개)
   */
  List<LostItem> findTop10ByEndUser(User user);

  /**
   * 분실물 등록
   *
   * @param userId 등록 요청한 사용자 ID
   * @param registerDto 분실물 등록 DTO
   * @param lostItemImage 분실물 이미지 파일
   * @return 등록된 분실물 ID
   * @throws FileOperationException 이미지 파일 처리에 실패한 경우
   */
  Long register(
      final Long userId, final LostItemRegisterDto registerDto, final MultipartFile lostItemImage);
}
