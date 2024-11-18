package com.itwillbs.bookjuk.dto.rent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RentResponseDTO {

    private List<RentDTO> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
