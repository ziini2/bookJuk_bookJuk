package com.itwillbs.bookjuk.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StatisticsResponseDTO {

    private List<StatisticsDTO> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
