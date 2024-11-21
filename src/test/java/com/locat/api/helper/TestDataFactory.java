package com.locat.api.helper;

import com.locat.api.domain.auth.dto.internal.KakaoUserInfo;
import com.locat.api.domain.geo.found.dto.internal.FoundItemRegisterDto;

public final class TestDataFactory {

  private TestDataFactory() {}

  public static KakaoUserInfo create(String id, String email) {
    return new KakaoUserInfo(id, new KakaoUserInfo.KakaoAccount(true, true, true, email));
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
