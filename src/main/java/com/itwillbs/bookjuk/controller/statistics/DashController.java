package com.itwillbs.bookjuk.controller.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DashController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {

        return "index";
    }
}
