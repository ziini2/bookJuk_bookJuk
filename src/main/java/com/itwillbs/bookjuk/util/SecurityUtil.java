package com.itwillbs.bookjuk.util;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.security.CustomOAuth2User;
import com.itwillbs.bookjuk.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    //현재 사용자의 권한(Role) 중 특정 권한을 가지고 있는지?
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }

    //현재 사용자의 모든 권한(Role) 조회
    public static List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return List.of(); //빈 리스트 반환
        }
        return authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());
    }

    //현재 사용자의 UserNum 값 조회
    public static Long getUserNum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserNum();
        } else if (principal instanceof OAuth2User) {
            return ((CustomOAuth2User) principal).getUserNum();
        }
        return null;
    }

    //현재 사용자의 userName 값 조회
    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getName();
        } else if (principal instanceof OAuth2User) {
            return ((CustomOAuth2User) principal).getName();
        }
        return null;
    }

    //유저의 권한이 변경되었을때 재로그인을 하지않고 최신 role 값을 불러오는 메서드
    public static boolean reloadUserRole(UserEntity user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //일반 유저일때 롤값 최신화
        if (user.getLoginType().equals(LoginType.MANUAL)){
            CustomUserDetails userDetails = new CustomUserDetails(user);
            Authentication newAuth = new UsernamePasswordAuthenticationToken
                    (userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return true;
        }
        //소셜로그인 유저일때 롤값 최신화
        else {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.info("oAuth2User: {}", oAuth2User);
            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("attributes: {}", attributes);


            //새로운 CustomOAuth2User 생성
            CustomOAuth2User newOAuth2User = new CustomOAuth2User(
                    oAuth2User.getOAuth2Response(),
                    user.getUserRole(),
                    oAuth2User.getUserNum(),
                    attributes
            );

            //새로운 인증 객채로 교체
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(newOAuth2User, null, newOAuth2User.getAuthorities()));
            return true;
        }

    }



}
