package com.locat.api.global.auth;

import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserType;
import com.locat.api.global.auth.jwt.PrincipalDetails;
import com.locat.api.infrastructure.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User 정보 가져오기
 * User entity로 변경 후,
 * database에 저장하기
 * PrincipalDetails return 하기
 * 추후 구현 필요! + LocatUserDetailsService 삭제?
 */
@Service
@RequiredArgsConstructor
public class LocatOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        return super.loadUser(userRequest);
    }
}