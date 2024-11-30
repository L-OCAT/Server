package com.locat.api.unit.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.controller.UserValidationController;
import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserValidationControllerTest {

  @InjectMocks private UserValidationController controller;
  @Mock private UserValidationService userValidationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("이메일 유효성 검사 테스트")
  @ParameterizedTest(name = "입력 {0} -> 결과 {1}")
  @CsvSource({"valid01@locat.kr, true", "invalid01@locat.kr, false"})
  void validateEmail(String email, boolean result) {
    // Given
    given(this.userValidationService.isExists(email, UserInfoValidationType.EMAIL))
        .willReturn(result);

    // When
    ResponseEntity<BaseResponse<Void>> response = controller.validateEmail(email);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(result ? HttpStatus.CONFLICT : HttpStatus.OK);
  }

  @DisplayName("닉네임 유효성 검사 테스트")
  @ParameterizedTest(name = "입력 {0} -> 결과 {1}")
  @CsvSource({"valid-nickname, true", "invalid-nickname, false"})
  void validateNickname(String nickname, boolean result) {
    // Given
    given(this.userValidationService.isExists(nickname, UserInfoValidationType.NICKNAME))
        .willReturn(result);

    // When
    ResponseEntity<BaseResponse<Void>> response = controller.validateNickname(nickname);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(result ? HttpStatus.CONFLICT : HttpStatus.OK);
  }
}
