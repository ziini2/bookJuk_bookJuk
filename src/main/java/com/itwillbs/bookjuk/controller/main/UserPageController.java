package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserPageController {
    @GetMapping("/")
    public String userMain() {
        log.info("userMain");
        log.info("userRole: {}", SecurityUtil.getUserRoles());
        log.info("userName: {}", SecurityUtil.getUserName());
        log.info("userNum: {}", SecurityUtil.getUserNum());
        //회원의 이름을 가져오기!

        if (SecurityUtil.hasRole("ROLE_INACTIVE")){
            return "redirect:/login/phone"; // 권한이 "ROLE_INACTIVE"라면 리다이렉트
        }
        if (SecurityUtil.hasRole("ROLE_ADMIN")){
            return "redirect:/admin/dashboard"; // 권한이 "ROLE_ADMIN"라면 리다이렉트
        }
        return "userMain";
    }
}
