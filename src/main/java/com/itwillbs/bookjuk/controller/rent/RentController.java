package com.itwillbs.bookjuk.controller.rent;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.service.rent.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RentController {

    private final RentService rentService;

    @GetMapping("/rent")
    public String rent(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<RentEntity> rentEntityList = rentService.findAll(page, size);

        rentEntityList.getContent().forEach(rentEntity -> {
            log.info("rentEntity: {}", rentEntity.toString());
        });


        model.addAttribute("rentEntityList", rentEntityList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rentEntityList.getTotalPages());




        return "rent/rent";
    }
}
