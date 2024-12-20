package com.locat.api.domain.geo.base.resolver;

import com.locat.api.domain.geo.base.dto.criteria.GeoItemSearchCriteria;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.dto.internal.FoundItemSearchDto;
import com.locat.api.domain.geo.lost.dto.internal.LostItemSearchDto;
import com.locat.api.global.exception.custom.InvalidParameterException;
import com.locat.api.global.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** 분실물 & 습득물 조회 요청 시, Request Parameter를 DTO로 바인딩하는 Resolver */
@Slf4j
public class GeoItemSearchArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String LOST_ITEM_URI = "/v1/items/losts";
  private static final String FOUND_ITEM_URI = "/v1/items/founds";

  /**
   * {@link GeoItemSearchCriteria}를 상속(구현)한 클래스인지 확인
   *
   * @param parameter 조건을 확인 할 {@link MethodParameter} 객체
   * @return {@link GeoItemSearchCriteria}를 상속(구현)한 클래스인지 여부
   */
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().isAssignableFrom(GeoItemSearchCriteria.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    final String requestUri = request.getRequestURI();

    final Double latitude = RequestUtils.getParameterOrDefault(request, "lat", Double.class, null);
    final Double longitude = RequestUtils.getParameterOrDefault(request, "lng", Double.class, null);
    final Double radius = RequestUtils.getParameterOrDefault(request, "r", Double.class, 50.0);
    final Boolean onlyMine =
        RequestUtils.getParameterOrDefault(request, "onlyMine", Boolean.class, true);
    final Point location = GeoUtils.toPoint(latitude, longitude);

    return switch (requestUri) {
      case LOST_ITEM_URI -> LostItemSearchDto.fromRequest(onlyMine, location, radius);
      case FOUND_ITEM_URI -> FoundItemSearchDto.fromRequest(onlyMine, location, radius);
      default -> throw new InvalidParameterException("Unexpected URI: " + requestUri);
    };
  }
}
