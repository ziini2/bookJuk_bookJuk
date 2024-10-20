package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.domain.login.UserRole;
import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;

    //회원가입 프로세스
    public boolean joinProcess(UserDTO userDTO){
        //DTO를 엔티티로 변환
        UserEntity userEntity = toEntity(userDTO);

        //저장된 유저 반환
        UserEntity saveUser = userRepository.save(userEntity);

        //저장 성공 여부 확인
        return userEntity != null ? true : false;
    }

    //UserDTO -> UserEntity 변환 메서드
    public UserEntity toEntity(UserDTO userDTO){
        return UserEntity.builder()
                .userId(userDTO.getUserId())
                .userPassword(userDTO.getUserPassword())
                .userName(userDTO.getUserName())
                .userBirthday(userDTO.getUserBirthday())
                .userEmail(userDTO.getUserEmail())
                .userPhone(userDTO.getUserPhone())
                .userRole(UserRole.ROLE_USER)
                .loginType(LoginType.MANUAL)
                .accepted(userDTO.isAccepted())
                .activate(true)
                .build();
    }
}
