package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final UserRepository userRepository;

    public boolean checkProcess(Map<String, String> checkData){

        //아이디 중복확인
        Optional<String> userId = Optional.ofNullable(checkData.get("userId"));
        //값이 공백 문자열인지 판단하고 공백이 아닌경우에만 optional 유지
        //isPresent() 유효한 값이 있을경우 true 반환
        if (userId.filter(id -> !id.isBlank()).isPresent()){
            return userRepository.existsByUserId(checkData.get("userId"));
        }

        //이메일 중복확인
        Optional<String> userEmail = Optional.ofNullable(checkData.get("userEmail"));
        if (userEmail.filter(email -> !email.isBlank()).isPresent()){
            return userRepository.existsByUserEmail(checkData.get("userEmail"));
        }

        //전화번호 중복확인
        Optional<String> userPhone = Optional.ofNullable(checkData.get("userPhone"));
        if (userPhone.filter(phone -> !phone.isBlank()).isPresent()){
            return userRepository.existsByUserPhone(checkData.get("userPhone"));
        }

        return false;
    }
}
