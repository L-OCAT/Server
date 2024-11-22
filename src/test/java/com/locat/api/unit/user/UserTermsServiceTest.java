package com.locat.api.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.locat.api.domain.terms.entity.Terms;
import com.locat.api.domain.terms.entity.TermsType;
import com.locat.api.domain.terms.service.TermsService;
import com.locat.api.domain.user.dto.internal.UserRegisterDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.association.UserTermsAgreement;
import com.locat.api.domain.user.service.impl.UserTermsServiceImpl;
import com.locat.api.global.exception.custom.InternalProcessingException;
import com.locat.api.helper.TestDataFactory;
import com.locat.api.infra.persistence.user.UserTermsAgreementRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

class UserTermsServiceTest {

  private static final List<Terms> MOCK_TERMS = TestDataFactory.createMockTerms();

  @InjectMocks private UserTermsServiceImpl service;
  @Mock private UserTermsAgreementRepository userTermsAgreementRepository;
  @Mock private TermsService termsService;

  @Captor private ArgumentCaptor<List<UserTermsAgreement>> userTermsAgreementsCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("회원가입 시 동의한 약관 목록을 저장한다.")
  void testRegister() {
    // Given
    User user = mock(User.class);
    given(this.termsService.findAll()).willReturn(MOCK_TERMS);
    UserRegisterDto registerDto = new UserRegisterDto("1", "TestUser", true, true, true, true);

    // When
    this.service.register(user, registerDto);

    // Then
    verify(this.userTermsAgreementRepository).saveAll(this.userTermsAgreementsCaptor.capture());
    assertThat(this.userTermsAgreementsCaptor.getValue())
        .hasSize(4)
        .hasOnlyElementsOfType(UserTermsAgreement.class)
        .allSatisfy(
            agreement -> {
              assertThat(agreement.getUser()).isEqualTo(user);
              assertThat(agreement.getTerms()).isIn(MOCK_TERMS);
            });
  }

  @Test
  @DisplayName("약관 타입에 맞는 약관을 반환한다.")
  void shouldReturnMatchingTerms() {
    // Given
    TermsType targetType = TermsType.TERMS_OF_SERVICE;

    // When
    Terms result =
        ReflectionTestUtils.invokeMethod(this.service, "getTermsByType", targetType, MOCK_TERMS);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getType()).isEqualTo(targetType);
  }

  @Test
  @DisplayName("약관 타입이 존재하지 않을 경우 예외를 발생시킨다.")
  void shouldThrowExceptionWhenTermsNotExists() {
    // Given
    TermsType targetType = TermsType.MARKETING_KAKAO_POLICY;

    // When / Then
    assertThatThrownBy(
            () ->
                ReflectionTestUtils.invokeMethod(
                    this.service, "getTermsByType", targetType, MOCK_TERMS))
        .isExactlyInstanceOf(InternalProcessingException.class);
  }
}
