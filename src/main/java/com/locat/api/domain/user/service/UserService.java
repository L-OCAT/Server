package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.criteria.AdminUserSearchCriteria;
import com.locat.api.domain.user.dto.internal.UserInfoDto;
import com.locat.api.domain.user.dto.internal.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  /**
   * 사용자 정보 저장
   *
   * @param user 사용자 정보
   * @return 저장된 사용자 정보
   */
  User save(final User user);

  /**
   * 사용자 ID로 사용자 조회
   *
   * @param id 사용자 ID
   * @return 사용자 정보
   * @throws NoSuchEntityException 해당 ID의 사용자가 없을 경우
   */
  Optional<User> findById(final Long id);

  /**
   * 사용자 이메일 해시로 사용자 조회
   *
   * @param email 사용자 이메일(Not Hashed)
   * @return 사용자 정보
   */
  Optional<User> findByEmail(final String email);

  /**
   * 사용자 OAuth ID로 사용자 조회
   *
   * @param oAuthId 사용자 OAuth ID
   * @return 사용자 정보
   */
  Optional<User> findByOAuthId(final String oAuthId);

  /**
   * 사용자 목록 조회
   *
   * @param criteria 검색 조건
   * @param pageable 페이징 정보
   * @return 사용자 목록
   */
  Page<UserInfoDto> findAll(AdminUserSearchCriteria criteria, Pageable pageable);

  /**
   * 사용자 정보 수정
   *
   * @param id 사용자 ID
   * @param infoUpdateDto 사용자 정보 수정 DTO
   * @return 수정된 사용자 정보
   */
  User update(final Long id, final UserInfoUpdateDto infoUpdateDto);

  /**
   * 사용자 탈퇴
   *
   * @param id 사용자 ID
   * @param reason 탈퇴 사유
   */
  void delete(final Long id, final String reason);
}
