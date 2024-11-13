package com.itwillbs.bookjuk.security;

import com.itwillbs.bookjuk.domain.login.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2Response oAuth2Response;
    private final UserRole userRole;
    private final Long userNum;
    private final Map<String, Object> attributes;


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userRole.toString();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

}
