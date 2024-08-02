package com.locat.api.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PrincipalDetails implements UserDetails {

    private final OAuth2User oAuth2User;
    private final String username;

    public PrincipalDetails(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
        this.username = this.oAuth2User.getAttribute("email");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(this.oAuth2User.getUserType.name())
        );
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}