package com.itwillbs.bookjuk.controller.login;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(HttpSession session){
        log.info("loginPage");
        if (session.getAttribute("userNum") != null){
            return "redirect:/";
        }
        return "/login/login";
    }
}
