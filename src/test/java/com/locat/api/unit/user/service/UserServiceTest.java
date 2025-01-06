package com.locat.api.unit.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import com.locat.api.domain.auth.template.OAuth2Template;
import com.locat.api.domain.auth.template.OAuth2TemplateFactory;
import com.locat.api.domain.user.dto.internal.UserInfoUpdateDto;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.service.UserValidationService;
import com.locat.api.domain.user.service.UserWithdrawalLogService;
import com.locat.api.domain.user.service.impl.UserServiceImpl;
import com.locat.api.global.exception.custom.DuplicatedException;
import com.locat.api.global.exception.custom.NoSuchEntityException;
import com.locat.api.global.utils.HashingUtils;
import com.locat.api.infra.persistence.user.UserQRepository;
import com.locat.api.infra.persistence.user.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

class UserServiceTest {

  private static final String BASE_EMAIL = "tester01@locat.kr";
  private static final String BASE_NICKNAME = "tester01";

  @InjectMocks private UserServiceImpl service;
  @Mock private UserRepository userRepository;
  @Mock private UserQRepository userQRepository;
  @Mock private OAuth2TemplateFactory oAuth2TemplateFactory;
  @Mock private UserValidationService userValidationService;
  @Mock private UserWithdrawalLogService userWithdrawalLogService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("사용자 Entity 저장에 성공한다.")
  void testSave() {
    // Given
    User mockUser = mock(User.class);

    // When & Then
    assertThatCode(() -> this.service.save(mockUser)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("사용자 존재 여부에 따라, ID로 조회에 성공한다.")
  void testFindById() {
    // Given
    given(this.userRepository.findById(1L)).willReturn(Optional.of(mock(User.class)));
    given(this.userRepository.findById(argThat(arg -> arg != 1L))).willReturn(Optional.empty());

    // When & Then
    Optional<User> result1 = this.service.findById(1L);
    Optional<User> result2 = this.service.findById(2L);
    Optional<User> result3 = this.service.findById(3L);

    // Then
    assertAll(
        () -> assertThat(result1).isPresent(),
        () -> assertThat(result2).isEmpty(),
        () -> assertThat(result3).isEmpty());
  }

  @Test
  @DisplayName("사용자 존재 여부에 따라, 이메일로 조회에 성공한다.")
  void testFindByEmail() {
    // Given
    String exsistingEmail = HashingUtils.hash("email1@locat.kr");
    given(this.userRepository.findByEmailHash(exsistingEmail))
        .willReturn(Optional.of(mock(User.class)));
    given(this.userRepository.findByEmailHash(argThat(arg -> !arg.equals(exsistingEmail))))
        .willReturn(Optional.empty());

    // When & Then
    Optional<User> result1 = this.service.findByEmail("email1@locat.kr");
    Optional<User> result2 = this.service.findByEmail("hello-world@naver.com");
    Optional<User> result3 = this.service.findByEmail("hey551me@gmail.com");

    // Then
    assertAll(
        () -> assertThat(result1).isPresent(),
        () -> assertThat(result2).isEmpty(),
        () -> assertThat(result3).isEmpty());
  }

  @Test
  @DisplayName("사용자 존재 여부에 따라, OAuth ID로 조회에 성공한다.")
  void testFindByOAuthId() {
    // Given
    String exsistingOAuthId = "5587109171122";
    given(this.userRepository.findByOauthId(exsistingOAuthId))
        .willReturn(Optional.of(mock(User.class)));
    given(this.userRepository.findByOauthId(argThat(arg -> !arg.equals(exsistingOAuthId))))
        .willReturn(Optional.empty());

    // When & Then
    Optional<User> result1 = this.service.findByOAuthId("5587109171122");
    Optional<User> result2 = this.service.findByOAuthId("001707.6083cb.0822");

    // Then
    assertAll(() -> assertThat(result1).isPresent(), () -> assertThat(result2).isEmpty());
  }

  @Test
  @DisplayName("사용자 목록을 조회할 수 있다.")
  void shouldFindAllUsers() {
    // Given
    given(this.userQRepository.findAllByCriteria(any(), any())).willReturn(Page.empty());

    // When
    this.service.findAll(null, null);

    // Then
    verify(this.userQRepository).findAllByCriteria(any(), any());
  }

  @Test
  @DisplayName("사용자 정보를 수정할 수 있다.")
  void shouldUpdateUserInfo() {
    // Given
    long id = 1L;
    UserInfoUpdateDto infoUpdateDto = new UserInfoUpdateDto("tester05@locat.kr", "tester05");

    this.stubbingUser(id);
    willDoNothing().given(this.userValidationService).validateEmail("test05@locat.kr");
    willDoNothing().given(this.userValidationService).validateNickname("tester05");

    // When
    User updatedUser = this.service.update(id, infoUpdateDto);

    // Then
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser.getEmail()).isEqualTo("tester05@locat.kr");
    assertThat(updatedUser.getNickname()).isEqualTo("tester05");
  }

  @DisplayName("사용자 정보 수정 실패 시나리오 테스트")
  @MethodSource("methodSoureForUpdateUserInfoFailCase")
  @ParameterizedTest(name = "{index}: [{0}] 상황에서 {2}를 입력하면, {3}가 발생해야 한다.")
  void shouldFailToUpdateUserInfo(
      String scenario,
      Long userId,
      UserInfoUpdateDto updateDto,
      Class<? extends Throwable> expectedException) {
    // Given
    Map<Class<? extends Throwable>, Consumer<UserInfoUpdateDto>> stubbingMaps =
        Map.of(
            NoSuchEntityException.class, dto -> this.handleNoSuchEntity(userId),
            DuplicatedException.class, dto -> this.handleDuplicatedException(userId, dto),
            RuntimeException.class, dto -> this.handleValidationFailed(userId, dto));
    // 시나리오에 따라 Stubbing 처리
    Consumer<UserInfoUpdateDto> stubbingHandler = stubbingMaps.get(expectedException);
    if (stubbingHandler != null) {
      stubbingHandler.accept(updateDto);
    }

    // When & Then
    assertThatThrownBy(() -> this.service.update(userId, updateDto))
        .isExactlyInstanceOf(expectedException);
  }

  private static Stream<Arguments> methodSoureForUpdateUserInfoFailCase() {
    return Stream.of(
        Arguments.of(
            "사용자가 존재하지 않음",
            1L,
            new UserInfoUpdateDto("tester01@locat.kr", "tester01"),
            NoSuchEntityException.class),
        Arguments.of(
            "이메일이 동일함",
            1L,
            new UserInfoUpdateDto("tester01@locat.kr", "tester05"),
            DuplicatedException.class),
        Arguments.of(
            "닉네임이 동일함",
            1L,
            new UserInfoUpdateDto("tester05@locat.kr", "tester01"),
            DuplicatedException.class),
        Arguments.of(
            "이메일 형식 오류",
            1L,
            new UserInfoUpdateDto("invalid-email", "tester05"),
            RuntimeException.class),
        Arguments.of(
            "닉네임 형식 오류",
            1L,
            new UserInfoUpdateDto("tester05@locat.kr", "invalid-nickname"),
            RuntimeException.class));
  }

  @Test
  @DisplayName("사용자를 탈퇴 처리할 수 있다.")
  void shouldDeleteUser() {
    // Given
    long id = 1L;
    String reason = "Reason to withdraw";
    String oAuthId = "5587109171122";

    User mockUser = User.builder().oauthId(oAuthId).build();
    given(this.userRepository.findById(id)).willReturn(Optional.of(mockUser));

    OAuth2Template mockTemplate = mock(OAuth2Template.class);
    given(this.oAuth2TemplateFactory.getById(oAuthId)).willReturn(mockTemplate);

    // When
    this.service.delete(id, reason);

    // Then
    verify(mockTemplate).withdrawal(oAuthId);
    verify(this.userWithdrawalLogService).save(id, reason);
  }

  @Test
  @DisplayName("사용자가 존재하지 않으면, 탈퇴 처리에 실패한다.")
  void shouldFailToDeleteUserIfUserNotFound() {
    // Given
    long id = 1L;
    String reason = "Reason to withdraw";

    given(this.userRepository.findById(id)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> this.service.delete(id, reason))
        .isExactlyInstanceOf(NoSuchEntityException.class);
  }

  private void handleNoSuchEntity(Long userId) {
    given(this.userRepository.findById(userId)).willReturn(Optional.empty());
  }

  private void handleDuplicatedException(Long userId, UserInfoUpdateDto updateDto) {
    this.stubbingUser(userId);
    if (updateDto.email().equals(BASE_EMAIL)) {
      willDoNothing().given(this.userValidationService).validateEmail(updateDto.email());
    }
    if (updateDto.nickname().equals(BASE_NICKNAME)) {
      willDoNothing().given(this.userValidationService).validateNickname(updateDto.nickname());
    }
  }

  private void handleValidationFailed(Long userId, UserInfoUpdateDto updateDto) {
    this.stubbingUser(userId);
    if (updateDto.email().contains("invalid")) {
      willThrow(RuntimeException.class)
          .given(this.userValidationService)
          .validateEmail(updateDto.email());
    } else if (updateDto.nickname().contains("invalid")) {
      willThrow(RuntimeException.class)
          .given(this.userValidationService)
          .validateNickname(updateDto.nickname());
    }
  }

  private void stubbingUser(long id) {
    User mockUser =
        User.builder()
            .email(BASE_EMAIL)
            .emailHash(HashingUtils.hash(BASE_EMAIL))
            .nickname(BASE_NICKNAME)
            .oauthId("5587109171122")
            .build();
    given(this.userRepository.findById(id)).willReturn(Optional.of(mockUser));
  }
}
