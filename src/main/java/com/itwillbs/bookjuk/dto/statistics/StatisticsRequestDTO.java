package com.itwillbs.bookjuk.dto.statistics;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class StatisticsRequestDTO {

    LocalDate startDate = LocalDate.now().minusMonths(1);
    LocalDate endDate = LocalDate.now();
    String pointOption = "전체";
    String genre = "전체";
    String storeName = "전체";
    int page = 0;
    int size = 20;
}
