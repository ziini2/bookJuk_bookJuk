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
        log.info("userNum: {}", session.getAttribute("userNum"));
        log.info("role: {}", session.getAttribute("role"));

        if (session.getAttribute("userNum") != null){
            return "redirect:/";
        }
        return "/login/login";
    }

    //소셜로그인 후 전화번호 입력 페이지로 이동
    @GetMapping("/phone")
    public String joinPhone(HttpSession session){
        log.info("joinPhone");
        log.info("userNum: {}", session.getAttribute("userNum"));
        log.info("role: {}", session.getAttribute("role"));
        return "/login/joinPhone";
    }
}
