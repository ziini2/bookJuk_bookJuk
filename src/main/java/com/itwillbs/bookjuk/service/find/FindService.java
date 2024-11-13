package com.itwillbs.bookjuk.service.find;

import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindService {
    private final UserRepository userRepository;
    private final MailSendService mailSendService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //아이디 찾기시 받아온 userName 과 Email 값이 일치하는 User 데이터 값 받아오기
    public Optional<String> findUserId(UserDTO userDTO){
        Optional<UserEntity> findUserEntity = userRepository
                .findByUserNameAndUserEmail(userDTO.getUserName(), userDTO.getUserEmail());
        return findUserEntity.map(UserEntity::getUserId);
    }

    //비밀번호 찾기 페이지에서 아이디와 비밀번호가 일치하는 지 확인후 휴대폰 인증
    public boolean findUserPass(UserDTO userDTO) {
        Optional<UserEntity> findUserEntity = userRepository
                .findByUserIdAndUserPhone(userDTO.getUserId(), userDTO.getUserPhone());
        return findUserEntity.isPresent();
    }

    //비밀번호 찾기 시 임시비밀번호 저장후 회원가입했던 메일로 반환
    public boolean updateUserPassword(UserDTO userDTO) {
        UserEntity userData = userRepository.findByUserId(userDTO.getUserId());
        //함수 임의의 임시비밀번호 생성
        String generatedPassword = PasswordGenerator.generatePassword();
        //시큐리티 사용하기 위해서 암호화로 저장
        userData.setUserPassword(bCryptPasswordEncoder.encode(generatedPassword));
        userRepository.save(userData);
        //가입했던 이메일로 임시비밀번호 전송
        return mailSendService.sendMail(userData.getUserEmail(), generatedPassword);
    }
}
