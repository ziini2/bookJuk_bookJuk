package com.itwillbs.bookjuk.controller.user;

import com.itwillbs.bookjuk.service.join.CheckService;
import com.itwillbs.bookjuk.service.join.JoinService;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPassController {

    private final CheckService checkService;
    private final JoinService joinService;

    @PostMapping("/passwordCheck")
    @ResponseBody
    public Map<String, String> passwordCheck(@RequestBody String userPassword) {
        userPassword = userPassword.trim();
        log.info("passwordCheck userPassword: {}", userPassword);
        if (checkService.userPasswordCheck(SecurityUtil.getUserNum(), userPassword)){
            log.info("check user password {}", checkService.userPasswordCheck(SecurityUtil.getUserNum(), userPassword));
            return Map.of("response", "success");
        }
        return Map.of("response", "fail");
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public Map<String, String> changePassword(@RequestBody String userPassword) {
        log.info("changePassword userPassword: {}", userPassword);
        userPassword = userPassword.trim();
        if (joinService.saveUserPassword(SecurityUtil.getUserNum(), userPassword)){
            return Map.of("response", "success");
        }
        return Map.of("response", "fail");
    }

}
