package com.itwillbs.bookjuk.controller.find;

import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.service.find.FindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class FindApiController {
    private final FindService findService;


    @PostMapping("/findId")
    @ResponseBody
    public Map<String, String> findId(@RequestBody UserDTO userDTO){
        log.info("findId: {}", userDTO);
        //이름과 이메일 정보로 받기
        //두가지가 일치한 값 디비조회 해서 있다면 true 반환하면서 아이디값 반환
        Optional<String> findUser = findService.findUserId(userDTO);
        if (findUser.isPresent()){
            String findUserId = findUser.get();
            log.info("findId: {}", findUserId);
            return Map.of("RESULT", "SUCCESS", "userId", findUserId);
        }
        return Map.of("RESULT", "FAIL");
    }

    @PostMapping("/findCheck")
    @ResponseBody
    public Map<String, String> findCheck(@RequestBody UserDTO userDTO){
        log.info("findCheck: {}", userDTO);
        //이름과 이메일 정보로 받기
        //두가지가 일치한 값 디비조회 해서 있다면 true 반환하면서 아이디값 반환
        boolean isFindUser = findService.findUserPass(userDTO);
        if (isFindUser){
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }

    @PostMapping("/findPass")
    @ResponseBody
    public Map<String, String> findPass(@RequestBody UserDTO userDTO){
        log.info("findPass: {}", userDTO);
        //임시비밀번호 저장후 임시비밀번호 이메일 전송
        boolean newPass = findService.updateUserPassword(userDTO);
        if (newPass){
            return Map.of("RESULT", "SUCCESS");
        }
        return Map.of("RESULT", "FAIL");
    }
}
