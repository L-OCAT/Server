package com.locat.api.domain.user.service;

import com.locat.api.domain.auth.entity.OAuth2ProviderToken;
import com.locat.api.domain.user.entity.User;

public interface UserTermsService {

  void registerByOAuth(User user, OAuth2ProviderToken token);
}
