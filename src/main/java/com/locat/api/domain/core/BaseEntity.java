package com.locat.api.domain.core;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity의 생성일시와 수정일시를 자동으로 관리하기 위한 추상 클래스입니다. <br>
 * 모든 Entity 클래스는 이 클래스 또는 {@link SecuredBaseEntity}를 상속받도록 구성해야 합니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private ZonedDateTime updatedAt;
}
