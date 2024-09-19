package com.locat.api.domain.auth.service;

import com.locat.api.global.auth.jwt.LocatTokenDto;

public interface AuthService {

  LocatTokenDto authenticate(String oAuthId);

  LocatTokenDto renew(String accessToken, String refreshToken);

  void sendVerificationEmail(String email);

  void verify(String email, String code);
}
