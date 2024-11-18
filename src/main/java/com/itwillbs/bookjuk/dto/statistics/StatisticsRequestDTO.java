package com.itwillbs.bookjuk.dto.statistics;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatisticsRequestDTO {

    LocalDate startDate = LocalDate.now().minusMonths(1);
    LocalDate endDate = LocalDate.now();
    String pointOption = "전체";
    String storeName = "전체";
    int page = 0;
    int size = 20;
}
