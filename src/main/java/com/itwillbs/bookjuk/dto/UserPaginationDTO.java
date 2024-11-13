package com.itwillbs.bookjuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaginationDTO {

    private List<UserPageBooksDTO> userPageBooksDTO;
    //페이지네이션 관련
    private int totalPages;
    private int currentPage;
    private boolean hasNext;
    private boolean hasPrevious;



}
