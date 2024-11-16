package com.itwillbs.bookjuk.dto.dashboard;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalStatisticsDTO {

    private String date;
    private long revenue;
    private long point;
    private long rental;
    private long delay;
    private long newUser;



}
