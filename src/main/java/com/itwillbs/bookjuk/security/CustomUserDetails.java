package com.itwillbs.bookjuk.security;


import com.itwillbs.bookjuk.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;

    public Long getUserNum(){
        return userEntity.getUserNum();
    }
    //사용자의 이름 불러오기
    public String getName(){
        return userEntity.getUserName();
    }

    //스프링 시큐리티 name 읜 userID 를 가져온다
    @Override
    public String getUsername() {
        return userEntity.getUserId();
    }

    @Override
    public String getPassword() {
        return userEntity.getUserPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getUserRole().name();
            }
        });
        return collection;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
