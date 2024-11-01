package com.itwillbs.bookjuk.security;

import com.itwillbs.bookjuk.domain.login.LoginType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NaverResponse implements OAuth2Response{
    private final Map<String, Object> attributes;

    //naver json 형태가 달라 생성자에서 "response" 객체 추출
    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }


    @Override
    public LoginType getProvider() {
        return LoginType.NAVER;
    }

    @Override
    public String getProviderId() {
        attributes.get("response");
        return attributes.getOrDefault("id", 0).toString();
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
