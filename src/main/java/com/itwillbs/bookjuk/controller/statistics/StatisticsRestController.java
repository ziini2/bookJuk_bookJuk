package com.itwillbs.bookjuk.controller.statistics;

import com.itwillbs.bookjuk.dto.statistics.StatisticsDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsRequestDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsResponseDTO;
import com.itwillbs.bookjuk.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    @GetMapping("/revenue")
    public ResponseEntity<StatisticsResponseDTO> getRevenueData(
            @RequestParam(defaultValue = "1000-01-01", name = "startDate") LocalDate startDate,
            @RequestParam(defaultValue = "1000-01-01", name = "endDate") LocalDate endDate,
            @RequestParam("pointOption") String pointOption,
            @RequestParam("genre") String genre,
            @RequestParam("storeName") String storeName,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size) {

        log.info("startDate: {}, endDate: {}, pointOption: {}, genre: {}, storeName: {}", startDate, endDate, pointOption, genre, storeName);

        LocalDate rowStartDate = startDate;
        LocalDate rowEndDate = endDate;

        if (startDate.equals(LocalDate.parse("1000-01-01")) || endDate.equals(LocalDate.parse("1000-01-01"))) {
            rowStartDate = LocalDate.now().minusMonths(1);
            rowEndDate = LocalDate.now();
        }


        StatisticsRequestDTO statisticsDTO = StatisticsRequestDTO.builder()
                .startDate(rowStartDate)
                .endDate(rowEndDate)
                .pointOption(pointOption)
                .genre(genre)
                .storeName(storeName)
                .page(page)
                .size(size)
                .build();

        log.info("statisticsDTO: {}", statisticsDTO);
        return ResponseEntity.ok(statisticsService.getRevenueData(statisticsDTO));
    }

    @GetMapping("/customer")
    public ResponseEntity<StatisticsResponseDTO> getCustomerData(
            @RequestParam(defaultValue = "1000-01-01", name = "startDate") LocalDate startDate,
            @RequestParam(defaultValue = "1000-01-01", name = "endDate") LocalDate endDate,
            @RequestParam(defaultValue = "전체", name = "gender") String gender,
            @RequestParam(defaultValue = "전체", name = "age") String age,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size) {

        LocalDate rowStartDate = startDate;
        LocalDate rowEndDate = endDate;

        if (startDate.equals(LocalDate.parse("1000-01-01")) || endDate.equals(LocalDate.parse("1000-01-01"))) {
            rowStartDate = LocalDate.now().minusMonths(1);
            rowEndDate = LocalDate.now();
        }

        StatisticsCustomerRequestDTO statisticsCustomerRequestDTO = new StatisticsCustomerRequestDTO(
                rowStartDate, rowEndDate, gender, age, page, size
        );

        log.info("statisticsCustomerRequestDTO: {}", statisticsCustomerRequestDTO);

        return ResponseEntity.ok(statisticsService.getCustomerData(statisticsCustomerRequestDTO));
    }

    @GetMapping("/revenueAll")
    public ResponseEntity<List<StatisticsDTO>> getRevenueAllData(
            @RequestParam(defaultValue = "1000-01-01", name = "startDate") LocalDate startDate,
            @RequestParam(defaultValue = "1000-01-01", name = "endDate") LocalDate endDate,
            @RequestParam(defaultValue = "전체", name = "pointOption") String pointOption,
            @RequestParam(defaultValue = "전체", name = "genre") String genre,
            @RequestParam(defaultValue = "전체", name = "storeName") String storeName
    ) {

        LocalDate rowStartDate = startDate;
        LocalDate rowEndDate = endDate;

        if (startDate.equals(LocalDate.parse("1000-01-01")) || endDate.equals(LocalDate.parse("1000-01-01"))){
            rowStartDate = LocalDate.now().minusMonths(1);
            rowEndDate = LocalDate.now();
        }

        StatisticsRequestDTO statisticsDTO = StatisticsRequestDTO.builder()
                .startDate(rowStartDate)
                .endDate(rowEndDate)
                .pointOption(pointOption)
                .genre(genre)
                .storeName(storeName)
                .build();

        return ResponseEntity.ok(statisticsService.getRevenueAllData(statisticsDTO));
    }

    @GetMapping("/customerAll")
    public ResponseEntity<List<StatisticsDTO>> getCustomerAllData(
            @RequestParam(defaultValue = "1000-01-01", name = "startDate") LocalDate startDate,
            @RequestParam(defaultValue = "1000-01-01", name = "endDate") LocalDate endDate,
            @RequestParam(defaultValue = "전체", name = "gender") String gender,
            @RequestParam(defaultValue = "전체", name = "age") String age
    ) {

        LocalDate rowStartDate = startDate;
        LocalDate rowEndDate = endDate;

        if (startDate.equals(LocalDate.parse("1000-01-01")) || endDate.equals(LocalDate.parse("1000-01-01"))) {
            rowStartDate = LocalDate.now().minusMonths(1);
            rowEndDate = LocalDate.now();
        }

        StatisticsCustomerRequestDTOPage statisticsCustomerRequestDTO = new StatisticsCustomerRequestDTOPage(
                rowStartDate, rowEndDate, gender, age);

        return ResponseEntity.ok(statisticsService.getCustomerAllData(statisticsCustomerRequestDTO));

    }

    public record StatisticsCustomerRequestDTO(
            LocalDate startDate, LocalDate endDate, String gender, String age, int page, int size) {
    }

    public record StatisticsCustomerRequestDTOPage(
            LocalDate startDate, LocalDate endDate, String gender, String age) {
    }
}



