package com.itwillbs.bookjuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRentInfoDTO {

    //대여정보 관련
    private LocalDate rentStart;
    private LocalDate rentEnd;
    private int rentPrice;
    private boolean rentStatus;

    //지점 코드 에 따른 지점 명
    private String storeName;

    //도서정보
    private String bookName;




}
