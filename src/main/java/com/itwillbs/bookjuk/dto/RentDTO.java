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
    private String userId;
    private String userName;
    private String userPhone;
    private String bookName;
    private Long bookNum;
    private String storeName;
    private LocalDate rentStart;
    private LocalDate rentEnd;
    private LocalDate returnDate;
    private String status;

}
