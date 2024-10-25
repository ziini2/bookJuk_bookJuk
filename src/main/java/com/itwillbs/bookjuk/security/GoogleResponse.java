package com.itwillbs.bookjuk.security;

import com.itwillbs.bookjuk.domain.login.LoginType;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleResponse implements OAuth2Response{
    private final Map<String, Object> attributes;

    @Override
    public LoginType getProvider() {
        return LoginType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attributes.getOrDefault("sub", 0).toString();
    }

    @Override
    public String getEmail() {
        return attributes.getOrDefault("email", 0).toString();
    }

    @Override
    public String getName() {
        return attributes.getOrDefault("name", 0).toString();
    }
}
