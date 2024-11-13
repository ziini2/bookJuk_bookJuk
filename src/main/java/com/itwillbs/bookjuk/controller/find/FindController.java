package com.itwillbs.bookjuk.controller.find;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class FindController {

    @GetMapping("/find")
    public String findPage(){
        log.info("findPage");
        return "/find/find";
    }
}
