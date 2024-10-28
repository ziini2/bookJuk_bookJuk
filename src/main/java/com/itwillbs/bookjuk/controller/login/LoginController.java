package com.itwillbs.bookjuk.controller.login;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    //일반회원 로그인
    @GetMapping("/login")
    public String loginPage(HttpSession session){
        log.info("loginPage");
        //세션에 값이 존재한다면 로그인 된것으로 판단하고 main 페이지로 이동
        if (session.getAttribute("userNum") != null){
            return "redirect:/";
        }
        return "/login/login";
    }

    //소셜로그인 후 전화번호 입력 페이지로 이동
    @GetMapping("/phone")
    public String joinPhone(HttpSession session){
        log.info("joinPhone");
        return "/login/joinPhone";
    }
}
