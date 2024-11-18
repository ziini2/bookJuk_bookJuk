package com.itwillbs.bookjuk.dto.statistics;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDTO {

    private String storeName;
    private String pointPayName;
    private long pointPrice;
    private LocalDate rentStart;
    private LocalDate rentEnd;
    private LocalDate returnDate;
    private long overdueDays;
    private String isbn;
    private String genre;
    private String author;
    private long userNum;

    // customer parameter
    private LocalDate joinDate;
    private long totalRentPrice;
    private long totalOverduePrice;
    private long totalPaymentPrice;
    private String gender;
    private int age;
    private long totalRentDays;
    private long totalOverdueDays;
    private long totalOverdueCount;
    private long totalRentCount;




}
