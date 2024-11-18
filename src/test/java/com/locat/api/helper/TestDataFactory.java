package com.locat.api.helper;

import com.locat.api.domain.auth.dto.internal.KakaoUserInfoDto;
import com.locat.api.domain.geo.found.dto.internal.FoundItemRegisterDto;

public final class TestDataFactory {

  private TestDataFactory() {}

  public static KakaoUserInfoDto create(String id, String email) {
    return new KakaoUserInfoDto(id, new KakaoUserInfoDto.KakaoAccount(true, true, true, email));
  }

  public static FoundItemRegisterDto create(
      String itemName, String description, String custodyLocation) {
    return FoundItemRegisterDto.builder()
        .itemName(itemName)
        .description(description)
        .custodyLocation(custodyLocation)
        .build();
  }
}
