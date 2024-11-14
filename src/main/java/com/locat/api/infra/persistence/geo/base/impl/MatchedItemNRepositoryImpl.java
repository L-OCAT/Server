package com.locat.api.infra.persistence.geo.base.impl;

import com.locat.api.domain.geo.base.entity.ColorCode;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.entity.FoundItem;
import com.locat.api.domain.geo.lost.entity.LostItem;
import com.locat.api.infra.persistence.geo.MatchedItemNRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MatchedItemNRepositoryImpl implements MatchedItemNRepository {

  @PersistenceContext private EntityManager entityManager;

  private static final String QUERY_COUNT_MATCHED_LOST_ITEMS =
      """
         SELECT COUNT(*)
         FROM lost_item li
         INNER JOIN lost_item_color_code licc ON licc.item_id = li.id
         WHERE li.category_id = :categoryId
         AND licc.color_id IN (:colorCodes)
         AND ST_Distance_Sphere(li.location, :location) <= :distance
         """;

  private static final String QUERY_COUNT_MATCHED_FOUND_ITEMS =
      """
            SELECT COUNT(*)
            FROM found_item fi
            INNER JOIN found_item_color_code ficc ON ficc.item_id = fi.id
            WHERE fi.category_id = :categoryId
            AND ficc.color_id IN (:colorCodes)
            AND ST_Distance_Sphere(fi.location, :location) <= :distance
            """;

  @Override
  public Long countMatchedLostItems(FoundItem foundItem) {
    return this.countMatchedItems(
        QUERY_COUNT_MATCHED_LOST_ITEMS,
        foundItem.getCategoryId(),
        foundItem.getColorCodes(),
        foundItem.getLocation(),
        DEFAULT_MATCH_DISTANCE.getValue());
  }

  @Override
  public Long countMatchedFoundItems(LostItem lostItem) {
    return this.countMatchedItems(
        QUERY_COUNT_MATCHED_FOUND_ITEMS,
        lostItem.getCategoryId(),
        lostItem.getColorCodes(),
        lostItem.getLocation(),
        DEFAULT_MATCH_DISTANCE.getValue());
  }

  private Long countMatchedItems(
      String queryString,
      Long categoryId,
      Set<ColorCode> colorCodes,
      Object location,
      Double distance) {
    Query nativeQuery = this.entityManager.createNativeQuery(queryString, Long.class);
    nativeQuery.setParameter("categoryId", categoryId);
    nativeQuery.setParameter("colorCodes", colorCodes.stream().map(ColorCode::getId).toList());
    nativeQuery.setParameter("location", location);
    nativeQuery.setParameter("distance", GeoUtils.toMeter(distance));
    return (Long) nativeQuery.getSingleResult();
  }
}
