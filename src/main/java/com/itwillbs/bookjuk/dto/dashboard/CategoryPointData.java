package com.itwillbs.bookjuk.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryPointData {

    private String category;
    private long rentalFee;
    private long overdueFee;
}
