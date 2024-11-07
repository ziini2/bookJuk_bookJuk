package com.itwillbs.bookjuk.controller.statistics;

import com.itwillbs.bookjuk.service.statistics.DashRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DashRestController {

    private final DashRestService dashRestService;

    @GetMapping("/new-customer")
    public Long getNumbersOfNewCustomers() {
        return dashRestService.getNumbersOfNewCustomers();
    }

    @GetMapping("/event")
    public Long getNumbersOfEvents() {
        return dashRestService.getNumbersOfEvents();
    }


}
