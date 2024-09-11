package com.locat.api.domain.user.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.user.service.UserValidationService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/validate")
public class UserValidationController {

  private final UserValidationService userValidationService;

  @GetMapping("/email")
  public ResponseEntity<BaseResponse<Boolean>> validateEmail(@RequestParam @Email String email) {
    final boolean result = this.userValidationService.isEmailExists(email);
    HttpStatus httpStatus = this.getHttpStatus(result);
    return ResponseEntity.status(httpStatus).build();
  }

  @GetMapping("/nickname")
  public ResponseEntity<BaseResponse<Boolean>> validateNickname(
      @RequestParam @NotEmpty String nickname) {
    final boolean result = this.userValidationService.isNicknameExists(nickname);
    HttpStatus httpStatus = this.getHttpStatus(result);
    return ResponseEntity.status(httpStatus).build();
  }

  private HttpStatus getHttpStatus(boolean result) {
    return result ? HttpStatus.CONFLICT : HttpStatus.OK;
  }
}
