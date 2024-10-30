package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckService {

    private final UserRepository userRepository;

    public boolean checkProcess(Map<String, String> checkData){
        log.info("checkProcess");

        //아이디 중복확인
        Optional<String> userId = Optional.ofNullable(checkData.get("userId"));
        //값이 공백 문자열인지 판단하고 공백이 아닌경우에만 optional 유지
        //isPresent() 유효한 값이 있을경우 true 반환
        if (userId.filter(id -> !id.isBlank()).isPresent()){
            String idValue = userId.get();
            log.info("userId Check : " + idValue);
            //반대로 해야함 중복확인시 null 이면 false 를 반환하기 때문에 true 로 변환해서 return
            return !userRepository.existsByUserId(idValue);
        }

        //이메일 중복확인
        Optional<String> userEmail = Optional.ofNullable(checkData.get("userEmail"));
        if (userEmail.filter(email -> !email.isBlank()).isPresent()){
            String emailValue = userEmail.get();
            log.info("userEmail Check : " + emailValue);
            return !userRepository.existsByUserEmail(emailValue);
        }

        //전화번호 중복확인
        Optional<String> userPhone = Optional.ofNullable(checkData.get("userPhone"));
        if (userPhone.filter(phone -> !phone.isBlank()).isPresent()){
            String phoneValue = userPhone.get();
            log.info("userPhone Check : " + phoneValue);
            return !userRepository.existsByUserPhone(phoneValue);
        }

        return false;
    }
}
