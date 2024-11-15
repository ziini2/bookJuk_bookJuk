package com.itwillbs.bookjuk.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserInfoController {


    @GetMapping("/info")
    public String userInfo() {
        return "user/userInfo";
    }



}
