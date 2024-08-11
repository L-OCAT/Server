package com.locat.api.domain.core;

import com.locat.api.global.security.LocatAuditorAware;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Entity의 생성자와 수정자를 자동으로 관리하기 위한 추상 클래스입니다. <br>
 * {@link LocatAuditorAware}를 통해 SecurityContext에서 현재 사용자 정보를 가져옵니다. <br>
 * 모든 Entity 클래스는 이 클래스 또는 {@link BaseEntity}를 상속받도록 구성해야 합니다.
 */
@Getter
@MappedSuperclass
public abstract class SecuredBaseEntity extends BaseEntity {

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private Long createdBy;

  @LastModifiedBy
  @Column(nullable = false)
  private Long updatedBy;
}
