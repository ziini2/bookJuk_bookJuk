package com.itwillbs.bookjuk.dto.rent;

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
    private Long bookNum;
    private String isbn;
    private String bookName;
    private String storeName;
    private LocalDate rentStart;
    private LocalDate rentEnd;
    private LocalDate returnDate;
    private String status;

}
