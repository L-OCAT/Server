package com.locat.api.domain.user.service;

import com.locat.api.domain.user.dto.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.EndUser;
import com.locat.api.global.exception.NoSuchEntityException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EndUserService {

  EndUser save(final EndUser user);

  /**
   * 사용자 ID로 사용자 조회
   *
   * @param id 사용자 ID
   * @return 사용자 정보
   * @throws NoSuchEntityException 해당 ID의 사용자가 없을 경우
   */
  Optional<EndUser> findById(final Long id);

  /**
   * 사용자 이메일 해시로 사용자 조회
   *
   * @param email 사용자 이메일(Not Hashed)
   * @return 사용자 정보
   */
  Optional<EndUser> findByEmail(final String email);

  /**
   * 사용자 OAuth ID로 사용자 조회
   *
   * @param oAuthId 사용자 OAuth ID
   * @return 사용자 정보
   */
  Optional<EndUser> findEndUserByOAuthId(final String oAuthId);

  Page<EndUser> findAll(Pageable pageable);

  /**
   * 사용자 정보 수정
   *
   * @param id 사용자 ID
   * @param infoUpdateDto 사용자 정보 수정 DTO
   * @return 수정된 사용자 정보
   */
  EndUser update(final Long id, final UserInfoUpdateDto infoUpdateDto);

  /**
   * 사용자 탈퇴
   *
   * @param id 사용자 ID
   * @param reason 탈퇴 사유
   */
  void delete(final Long id, final String reason);
}