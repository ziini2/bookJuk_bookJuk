package com.itwillbs.bookjuk.JoinService;

import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.exception.ValidationException;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.service.join.JoinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JoinTest {

    private static final Logger log = LoggerFactory.getLogger(JoinTest.class);
    @Autowired
    private JoinService joinService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDatabase(){
        //테스트 시작전 데이터 초기화
        if (userRepository.existsByUserId("test123")){
            UserEntity userEntity = userRepository.findByUserId("test123");
            userRepository.deleteById(userEntity.getUserNum());
            log.info("UserId 'test123' 유저 데이터 삭제 완료");
        }
    }


    @Test
    public void joinProcessTest(){
        //회원가입 테스트 예외 테스트
        //비정상적으로 checkService 를 통하지않고 회원가입을 시도하는 경우도 체크!
        UserDTO userDTO = UserDTO.builder()
                .userId("test123")
                .userPassword("12345678")
                .userName("테스트1")
                .userBirthday("2024년10월10일")
                .userEmail("test1@test.com")
                .userPhone("010-1234-5678")
                .accepted(true)
                .build();
        boolean result = joinService.joinProcess(userDTO);
        log.info("첫 번째 회원가입 성공 여부: {}", result);

        //예외 발생 테스트
        ValidationException exception = assertThrows(
                ValidationException.class, () -> joinService.joinProcess(userDTO)
        );
        //예외 메세지 검증
        System.out.println("테스트 예외 메세지: " + exception.getMessage());
    }

}
