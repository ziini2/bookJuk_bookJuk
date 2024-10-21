package com.itwillbs.bookjuk.controller.join;


import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.service.join.CheckService;
import com.itwillbs.bookjuk.service.join.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final CheckService checkService;

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

    //회원가입
    @PostMapping("/join")
    @ResponseBody
    public Map<String, String> join(@RequestBody UserDTO userDTO) {
        //먼저 가져오 userDTO.userID 값이 같은 아이디가 있는지 없는 중복확인부터 해야함
        boolean isSave = joinService.joinProcess(userDTO);
        if (isSave){
            return Map.of("RESULT", "SUCCESS");
        }

        return Map.of("RESULT", "FAIL");
    }



}
