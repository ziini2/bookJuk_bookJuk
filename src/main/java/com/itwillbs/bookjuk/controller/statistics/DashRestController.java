package com.itwillbs.bookjuk.controller.statistics;

import com.itwillbs.bookjuk.dto.dashboard.PointResponseDTO;
import com.itwillbs.bookjuk.dto.dashboard.ResponseDTO;
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
    public ResponseEntity<List<TotalStatisticsDTO>> getTotalStatistics(
            @RequestParam(defaultValue = "week", name="period") String period) {
        return ResponseEntity.ok(dashRestService.getConsolidatedStatisticsWithFilledDates(period));
    }

    // stores 드롭다운에 표기될 지점명
    @GetMapping("stores")
    public ResponseEntity<List<String>> getStores() {
        return ResponseEntity.ok(dashRestService.getStores());
    }

    @GetMapping("genre")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(dashRestService.getGenres());
    }

    @GetMapping("period/point")
    public ResponseEntity<PointResponseDTO> getPointStatistics(
            @RequestParam(defaultValue = "week", name="period") String period,
            @RequestParam(defaultValue = "", name="storeList") List<String> storeList,
            @RequestParam(defaultValue = "전체", name="") String salesOption) {
        return ResponseEntity.ok(dashRestService.getPointStatistics(period, storeList, salesOption));
    }

    @GetMapping("period/delay")
    public ResponseEntity<ResponseDTO> getDelayStatistics(
            @RequestParam(defaultValue = "week", name="period") String period,
            @RequestParam(defaultValue = "", name="storeList") List<String> storeList) {
        return ResponseEntity.ok(dashRestService.getDelayStatistics(period, storeList));
    }

    @GetMapping("period/revenue")
    public ResponseEntity<ResponseDTO> getRevenueStatistics(
            @RequestParam(defaultValue = "week", name="period") String period) {
        return ResponseEntity.ok(dashRestService.getRevenueStatistics(period));
    }

    @GetMapping("period/customer")
    public ResponseEntity<ResponseDTO> getCustomerStatistics(
            @RequestParam(defaultValue = "week", name="period") String period) {
        return ResponseEntity.ok(dashRestService.getCustomerStatistics(period));
    }




}
