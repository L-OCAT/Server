package com.locat.api.helper;

import com.locat.api.domain.auth.dto.internal.KakaoUserInfo;
import com.locat.api.domain.geo.base.entity.Category;
import com.locat.api.domain.geo.base.utils.GeoUtils;
import com.locat.api.domain.geo.found.dto.internal.FoundItemRegisterDto;
import com.locat.api.domain.geo.lost.dto.internal.LostItemRegisterDto;
import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import java.util.List;

public final class TestDataFactory {

  private TestDataFactory() {}

  public static List<Terms> createMockTerms() {
    return List.of(
        Terms.builder().type(TermsType.TERMS_OF_SERVICE).title("이용약관").build(),
        Terms.builder().type(TermsType.PRIVACY_POLICY).title("개인정보처리방침").build(),
        Terms.builder().type(TermsType.LOCATION_POLICY).title("위치정보이용약관").build(),
        Terms.builder().type(TermsType.MARKETING_POLICY).title("마케팅약관").build());
  }

  public static KakaoUserInfo create(String id, String email) {
    return new KakaoUserInfo(id, new KakaoUserInfo.KakaoAccount(true, true, true, email));
  }

  public static FoundItemRegisterDto create(
      String itemName, String description, String custodyLocation) {
    return FoundItemRegisterDto.builder()
        .itemName(itemName)
        .description(description)
        .custodyLocation(custodyLocation)
        .location(GeoUtils.toPoint(37.5665, 126.9780)) // 서울시청
        .build();
  }

  public static LostItemRegisterDto create(
      String itemName, String description, Integer gratuity, Boolean isWillingToPayGratuity) {
    return LostItemRegisterDto.builder()
        .itemName(itemName)
        .description(description)
        .gratuity(gratuity)
        .isWillingToPayGratuity(isWillingToPayGratuity)
        .location(GeoUtils.toPoint(37.5665, 126.9780)) // 서울시청
        .build();
  }

  public static Category createCategory(long id, String name) {
    return Category.builder().id(id).name(name).build();
  }

  public static Category createCategory(String name) {
    return createCategory(1L, name);
  }
}
