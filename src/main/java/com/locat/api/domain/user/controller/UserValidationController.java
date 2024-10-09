package com.locat.api.domain.user.controller;

import com.locat.api.domain.common.dto.BaseResponse;
import com.locat.api.domain.user.enums.UserInfoValidationType;
import com.locat.api.domain.user.service.UserValidationService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/validate")
public class UserValidationController {

  private final UserValidationService userValidationService;

  @GetMapping("/email")
  public ResponseEntity<BaseResponse<Boolean>> validateEmail(@RequestParam @Email String email) {
    final boolean result = this.userValidationService.isExists(email, UserInfoValidationType.EMAIL);
    HttpStatus httpStatus = this.getHttpStatus(result);
    return ResponseEntity.status(httpStatus).build();
  }

  @GetMapping("/nickname")
  public ResponseEntity<BaseResponse<Boolean>> validateNickname(
      @RequestParam @NotEmpty String nickname) {
    final boolean result =
        this.userValidationService.isExists(nickname, UserInfoValidationType.NICKNAME);
    HttpStatus httpStatus = this.getHttpStatus(result);
    return ResponseEntity.status(httpStatus).build();
  }

  private HttpStatus getHttpStatus(boolean result) {
    return result ? HttpStatus.CONFLICT : HttpStatus.OK;
  }
}
