package com.locat.api.infrastructure.aws.dynamodb;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DynamoDB에 저장하는 Item에 대한 기본적인 CRUD 작업을 제공하는 인터페이스
 *
 * @param <T> 관리할 Item
 * @param <ID> Item의 식별자 타입
 */
public interface DynamoDbCrudRepository<T, ID> {

  /**
   * Item를 저장합니다.
   *
   * @param Item 저장할 Item
   * @return 저장된 Item
   */
  T save(T item);

  /**
   * Item를 식별자로 조회합니다.
   *
   * @param id 조회할 Item의 식별자 (never {@code null})
   * @return 조회된 Item, 없을 경우 {@link Optional#empty()}
   */
  Optional<T> findById(ID id);

  /**
   * 모든 Item를 조회합니다.
   *
   * @return 모든 Item 목록
   */
  List<T> findAll();

  Page<T> findAll(Pageable pageable);

  /**
   * Item를 수정합니다.
   *
   * @param Item 수정할 Item
   * @return 수정된 Item
   */
  T update(T item);

  /**
   * Item를 식별자로 삭제합니다.
   *
   * @param id 삭제할 Item의 식별자 (never {@code null})
   */
  void delete(ID id);

  /**
   * Item의 총 개수를 조회합니다.
   *
   * @return Item의 총 개수
   */
  Long countAll();
}
