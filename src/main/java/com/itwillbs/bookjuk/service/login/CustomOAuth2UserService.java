package com.itwillbs.bookjuk.service.login;

import com.itwillbs.bookjuk.domain.login.UserRole;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.security.CustomOAuth2User;
import com.itwillbs.bookjuk.security.GoogleResponse;
import com.itwillbs.bookjuk.security.NaverResponse;
import com.itwillbs.bookjuk.security.OAuth2Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService 의 구현체
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.getAttributes().toString());

        log.info("Naver OAuth2 Attributes: {}", oAuth2User.getAttributes());
        log.info("Extracted Response: {}", oAuth2User.getAttributes().get("response"));



        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else return null;


        //email 을 확인하여 중복되는지 아닌지 판단하기 위함
        UserEntity exitsData = userRepository.findByUserEmail(oAuth2Response.getEmail());
        //데이터 베이스에 저장후 user 의 고유한 키값 을 저장하기 위함
        Long userNum;
        //User_Role 설정
        UserRole userRole = UserRole.ROLE_INACTIVE;

        if (exitsData == null){
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(oAuth2Response.getProviderId());
            //임시로 user_phone 테이블을 채워두고 로그인이후 전화번호를 따로 입력하지 않을시 << 대여 할수 없게 만드는 조건 추가해야함!
            userEntity.setUserPhone(oAuth2Response.getProvider().toString() + "_" + oAuth2Response.getEmail());
            userEntity.setUserName(oAuth2Response.getName());
            userEntity.setUserEmail(oAuth2Response.getEmail());
            userEntity.setLoginType(oAuth2Response.getProvider());
            userEntity.setAccepted(true);
            userEntity.setActivate(true);
            userEntity.setUserRole(userRole);

            userRepository.save(userEntity);

            userNum = userEntity.getUserNum();
        }
        else {
            //기존 유저가 존재하면 혹시 받아온 값중 변경사항이 있으면 업데이트 하기
            exitsData.setUserName(oAuth2Response.getName());
            exitsData.setUserEmail(oAuth2Response.getEmail());

            userRole = exitsData.getUserRole();
            userNum = exitsData.getUserNum();

            userRepository.save(exitsData);
        }
        return new CustomOAuth2User(oAuth2Response, userRole, userNum, oAuth2User.getAttributes());
    }

}
