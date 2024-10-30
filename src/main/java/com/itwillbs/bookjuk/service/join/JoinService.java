package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.domain.login.UserRole;
import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.exception.ValidationException;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //회원가입 프로세스
    public boolean joinProcess(UserDTO userDTO){
        //UserDTO 유효성 검사
        validateUser(userDTO);

        //DTO를 엔티티로 변환
        UserEntity userEntity = toEntity(userDTO);

        //비밀번호 암호화 후 저장 (단방향 해쉬 암호화)
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userDTO.getUserPassword()));

        //저장된 유저 반환
        UserEntity saveUser = userRepository.save(userEntity);

        //저장 성공 여부 확인
        return saveUser != null ? true : false;
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

    //유효성 검사 메서드
    private void validateUser(UserDTO userDTO) {
        //아이디 중복 검사
        if (userRepository.existsByUserId(userDTO.getUserId())) {
            log.warn("중복된 아이디로 가입 시도: {}", userDTO.getUserId());
            throw new ValidationException("이미 사용 중인 아이디 입니다.");
        }

        //이메일 중복 검사
        if (userRepository.existsByUserEmail(userDTO.getUserEmail())) {
            log.warn("중복된 이메일로 가입 시도: {}", userDTO.getUserEmail());
            throw new ValidationException("이미 등록된 이메일입니다.");
        }

        //비밀번호 길이 검사
        if (userDTO.getUserPassword().length() < 8) {
            throw new ValidationException("비밀번호는 최소 8자 이상이어야 합니다.");
        }

        //이메일 형식 검사
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!Pattern.matches(emailPattern, userDTO.getUserEmail())) {
            throw new ValidationException("유효하지 않은 이메일 형식입니다.");
        }
    }

    //소셜로그인 전화번호 저장
    public boolean saveUserPhone(Long userNum, String userPhone) {
        log.info("saveUserPhone: {}", userNum);
        UserEntity user = userRepository.findByUserNum(userNum);
        if (user == null) {
            return false;
        }
        user.setUserPhone(userPhone);
        user.setUserRole(UserRole.ROLE_USER);
        UserEntity updateUser = userRepository.save(user);

        return SecurityUtil.reloadUserRole(updateUser);
    }
}
