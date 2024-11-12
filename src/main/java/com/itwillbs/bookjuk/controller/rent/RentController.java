package com.itwillbs.bookjuk.controller.rent;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.service.rent.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RentController {

    private final RentService rentService;

    @GetMapping("/rent")
    public String rent(Model model) {

        List<RentEntity> rentEntityList = rentService.findAll();
        model.addAttribute("rentEntityList", rentEntityList);

        return "rent/rent";
    }




}

