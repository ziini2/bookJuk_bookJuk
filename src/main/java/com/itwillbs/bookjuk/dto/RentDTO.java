package com.itwillbs.bookjuk.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentDTO {

    private Long rentNum;
    private Long userNum;
    private Long storeCode;
    private Long bookId;
    private Integer rentPrice;
    private LocalDate rentStart;
    private LocalDate rentEnd;
    private LocalDate returnDate;
    private Byte rentStatus;

}
