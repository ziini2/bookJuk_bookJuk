package com.itwillbs.bookjuk.controller.join;

import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.service.join.CheckService;
import com.itwillbs.bookjuk.service.join.JoinService;
import com.itwillbs.bookjuk.service.join.SmsService;
import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class JoinController {

    private final JoinService joinService;
    private final CheckService checkService;
    private final SmsService smsService;

    //중복확인
    @PostMapping("/checkData")
    @ResponseBody
    public Map<String, String> checkProcess(@RequestBody Map<String, String> checkData) {
        log.info("checkData: {}", checkData);
        boolean isCheck = checkService.checkProcess(checkData);
        if (isCheck){
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }

    //SMS 코드 보내기 요청
    @PostMapping("/sendSmsCode")
    @ResponseBody
    public Map<String, String> checkPhone(@RequestBody String userPhone, HttpSession session) {
        String code = smsService.sendSMS(userPhone);
        if (session.getAttribute("code") != null ) {
            session.removeAttribute("code");
        }
        session.setAttribute("code", code);
        //세션에 코드 유효시간 처리
        session.setMaxInactiveInterval(30);
        return Map.of("RESULT", "SUCCESS", "code", code);
    }

    //SMS 코드 보내기 요청 테스트
    @PostMapping("/sendSmsCodeTest")
    @ResponseBody
    public Map<String, String> checkPhoneTest(@RequestBody String userPhone, HttpSession session) {
        String code = smsService.sendSMSTest(userPhone);
        if (session.getAttribute("code") != null ) {
            session.removeAttribute("code");
        }
        session.setAttribute("code", code);
        //세션에 코드 유효시간 처리
        session.setMaxInactiveInterval(30);
        return Map.of("RESULT", "SUCCESS", "code", code);
    }

    //휴대폰 인증번호 검증
    @PostMapping("/codeValidate")
    @ResponseBody
    public Map<String, String> codeValidate(@RequestBody String code, HttpSession session) {
        log.info("codeValidate : {}", code);
        //sms 인증코드 불러오기
        String systemCode = (String) session.getAttribute("code");
        //sms 인증코드와 유저 코드 비교
        if (code.equals(systemCode)){
            session.removeAttribute("code");
            //일치시 세션에 성공값 저장
            session.setAttribute("RESULT", "SUCCESS");
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }

    //휴대폰 번호 인증 완료했는지 확인
    @PostMapping("/verifySmsCode")
    @ResponseBody
    public Map<String, Boolean> verifySmsCode(HttpSession session) {
        log.info("verifySmsCode : {}", session.getAttribute("RESULT"));
        if (session.getAttribute("RESULT") != null){
            return Map.of("RESULT", true);
        }
        else
            return Map.of("RESULT", false);
    }

    //==========================================

    //회원가입
    @PostMapping("/join")
    @ResponseBody
    public Map<String, String> join(@RequestBody UserDTO userDTO, HttpSession session) {
        //먼저 가져오 userDTO.userID 값이 같은 아이디가 있는지 없는 중복확인부터 해야함
        boolean isSave = joinService.joinProcess(userDTO);
        if (isSave){
            session.removeAttribute("RESULT");
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }

    //간편로그인 시 전화번호 저장 페이지
    @PostMapping("/join/phone")
    @ResponseBody
    public Map<String, String> phoneSave(@RequestBody String userPhone) {
        log.info("phoneSave");
        log.info("userPhone: {}", userPhone);
        //1. 전화번호 입력페이지로 이동시키고 전화번호 등록후 USER_ROLE값 변경 시켜주면됨!
        //  - 전화번호 입력하는 페이지 보여주고
        //  - 전화번호 중복확인 - userCheck 로 보내서 체크
        //  - 전화번호 중복확인후 저장 - saveUserPhone <<
        //  - 전화번호 저장후 리다이렉트 메인으로 - SUCCESS 반환해서 js에서 메인으로 리다이렉트
        Long userNum = SecurityUtil.getUserNum();
        boolean isSaved = joinService.saveUserPhone(userNum, userPhone);
        log.info("isSaved: {}", isSaved);
        if (isSaved){
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }
}
