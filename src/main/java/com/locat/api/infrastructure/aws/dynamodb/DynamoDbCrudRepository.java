package com.locat.api.infrastructure.aws.dynamodb;

import java.util.List;
import java.util.Optional;

/**
 * DynamoDB에 저장하는 Item에 대한 기본적인 CRUD 작업을 제공하는 인터페이스
 * @param <T> 관리할 Item
 * @param <ID> Item의 식별자 타입
 */
public interface DynamoDbCrudRepository<T, ID> {

  /**
   * Entity를 저장합니다.
   * @param entity 저장할 Entity
   * @return 저장된 Entity
   */
  T save(T entity);

  /**
   * Entity를 식별자로 조회합니다.
   * @param id 조회할 Entity의 식별자 (never {@code null})
   * @return 조회된 Entity, 없을 경우 {@link Optional#empty()}
   */
  Optional<T> findById(ID id);

  /**
   * 모든 Entity를 조회합니다.
   * @return 모든 Entity 목록
   */
  List<T> findAll();

  /**
   * Entity를 수정합니다.
   * @param entity 수정할 Entity
   * @return 수정된 Entity
   */
  T update(T entity);

  /**
   * Entity를 식별자로 삭제합니다.
   * @param id 삭제할 Entity의 식별자 (never {@code null})
   */
  void delete(ID id);
}
