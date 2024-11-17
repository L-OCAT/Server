package com.locat.api.global.config;

import com.locat.api.domain.geo.base.resolver.GeoItemSearchArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  private static final List<HandlerMethodArgumentResolver> CUSTOM_ARGUMENT_RESOLVERS =
      List.of(new GeoItemSearchArgumentResolver());

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.addAll(CUSTOM_ARGUMENT_RESOLVERS);
  }
}
