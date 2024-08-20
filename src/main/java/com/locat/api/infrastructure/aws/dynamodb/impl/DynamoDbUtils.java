package com.locat.api.infrastructure.aws.dynamodb.impl;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public final class DynamoDbUtils {

  private DynamoDbUtils() {
    // Utility class
  }

  @Nullable public static <T> Map<String, AttributeValue> getLastEvaluatedKey(PageIterable<T> pageIterable) {
    return pageIterable.stream().map(Page::lastEvaluatedKey).findFirst().orElse(null);
  }

  public static <T> List<T> toList(PageIterable<T> pageIterable) {
    return pageIterable.items().stream().toList();
  }
}
