package com.itwillbs.bookjuk.controller.statistics;

import com.itwillbs.bookjuk.dto.dashboard.TotalStatisticsDTO;
import com.itwillbs.bookjuk.service.statistics.DashRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashRestController {

    private final DashRestService dashRestService;

    // daily dashboard
    @GetMapping("daily/new-customer")
    public Long getNumbersOfNewCustomers() {
        return dashRestService.getNumbersOfNewCustomers();
    }

    @GetMapping("daily/event")
    public Long getNumbersOfEvents() {
        return dashRestService.getNumbersOfEvents();
    }

    @GetMapping("daily/revenue")
    public Long getDailyRevenue() {
        return dashRestService.getDailyRevenue();
    }

    @GetMapping("daily/point")
    public Long getDailyPoint() {
        return dashRestService.getDailyPoint();
    }

    @GetMapping("daily/rental")
    public Long getDailyRental() {
        return dashRestService.getDailyRental();
    }

    @GetMapping("daily/delay")
    public Long getDailyDelay() {
        return dashRestService.getDailyDelay();
    }

    // weekly or more dashboard
    @GetMapping("period/total")
    public ResponseEntity<List<TotalStatisticsDTO>> getTotalStatistics(@RequestParam(defaultValue = "week") String period) {
        return ResponseEntity.ok(dashRestService.getConsolidatedStatisticsWithFilledDates(period));
    }


}
