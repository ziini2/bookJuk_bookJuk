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

    @GetMapping("/revenue")
    public Long getDailyRevenue() {
        return dashRestService.getDailyRevenue();
    }

    @GetMapping("/point")
    public Long getDailyPoint() {
        return dashRestService.getDailyPoint();
    }

    @GetMapping("/rental")
    public Long getDailyRental() {
        return dashRestService.getDailyRental();
    }

    @GetMapping("/delay")
    public Long getDailyDelay() {
        return dashRestService.getDailyDelay();
    }


}
