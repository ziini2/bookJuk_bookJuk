package com.itwillbs.bookjuk.controller.login;


import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    //일반회원 로그인
    @GetMapping
    public String loginPage(){
        log.info("loginPage");
        //로그인 된유저 인지 아닌지 판단하고 main 페이지로 이동
        if (SecurityUtil.getUserNum() != null || SecurityUtil.hasRole("ROLE_INACTIVE")){
            return "redirect:/";
        }
        return "/login/login";
    }

    //소셜로그인 후 전화번호 입력 페이지로 이동
    @GetMapping("/phone")
    public String joinPhone(){
        log.info("joinPhone");
        return "/login/joinPhone";
    }
}
