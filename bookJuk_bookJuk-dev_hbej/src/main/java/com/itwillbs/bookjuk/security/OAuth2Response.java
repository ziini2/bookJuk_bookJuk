package com.itwillbs.bookjuk.security;


import com.itwillbs.bookjuk.domain.login.LoginType;

public interface OAuth2Response {
    //제공자 (ex. naver, google)
    LoginType getProvider();

    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    //이메일
    String getEmail();

    //사용자 실명 (또는 사용자 닉네임)
    String getName();
}
