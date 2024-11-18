package com.itwillbs.bookjuk.controller.statistics;

import com.itwillbs.bookjuk.dto.statistics.StatisticsDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsRequestDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsResponseDTO;
import com.itwillbs.bookjuk.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    @GetMapping("/revenue")
    public ResponseEntity<StatisticsResponseDTO> getRevenueData(@RequestBody StatisticsRequestDTO statisticsDTO) {
        return ResponseEntity.ok(statisticsService.getRevenueData(statisticsDTO));
    }


}
