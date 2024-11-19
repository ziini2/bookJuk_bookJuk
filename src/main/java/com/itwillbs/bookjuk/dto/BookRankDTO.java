package com.itwillbs.bookjuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRankDTO {

    private String bookImage;
    private String bookName;
    private Long rentCount;
}
