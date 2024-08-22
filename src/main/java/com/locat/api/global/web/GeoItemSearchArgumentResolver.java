package com.locat.api.global.web;

import com.locat.api.domain.geo.base.dto.GeoItemSearchCriteria;
import com.locat.api.domain.geo.found.dto.FoundItemSearchDto;
import com.locat.api.domain.geo.lost.dto.LostItemSearchDto;
import com.locat.api.global.exception.ParameterValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** 분실물 & 습득물 조회 요청 시, Request Parameter를 DTO로 바인딩하는 Resolver */
@Slf4j
public class GeoItemSearchArgumentResolver implements HandlerMethodArgumentResolver {

  private static final Class<?> SUPPORTED_CLASS = GeoItemSearchCriteria.class;
  private static final String LOST_ITEM_URI = "v1/losts";
  private static final String FOUND_ITEM_URI = "v1/founds";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return SUPPORTED_CLASS.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    HttpServletRequest request = getHttpServletRequest(webRequest);
    try {
      final String requestUri = request.getRequestURI();

      final Double latitude = parseRequiredParameter(request, "lat", Double.class);
      final Double longitude = parseRequiredParameter(request, "lng", Double.class);
      final Integer radius = parseRequiredParameter(request, "r", Integer.class);
      final Boolean onlyMine = parseRequiredParameter(request, "onlyMine", Boolean.class);
      final String sort = parseRequiredParameter(request, "s", String.class);
      final Point location = new Point(longitude, latitude);

      return switch (requestUri) {
        case LOST_ITEM_URI -> LostItemSearchDto.fromRequest(onlyMine, location, radius, sort);
        case FOUND_ITEM_URI -> FoundItemSearchDto.fromRequest(onlyMine, location, radius, sort);
        default -> throw new IllegalArgumentException("Unexpected URI: " + requestUri);
      };
    } catch (IllegalArgumentException | ClassCastException e) {
      throw new ParameterValidationException(e.getMessage());
    }
  }

  private HttpServletRequest getHttpServletRequest(NativeWebRequest webRequest) {
    return (HttpServletRequest) webRequest.getNativeRequest();
  }

  private static <T> T parseRequiredParameter(
      HttpServletRequest request, String parameterName, Class<T> clazz) {
    String parameter = request.getParameter(parameterName);
    if (parameter == null) {
      throw new IllegalArgumentException("Required parameter \"" + parameterName + "\" is missing");
    }
    return clazz.cast(parameter);
  }
}
