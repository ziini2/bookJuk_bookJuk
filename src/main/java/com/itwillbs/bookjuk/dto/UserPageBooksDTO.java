package com.itwillbs.bookjuk.dto;


import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageBooksDTO {

    private Long booksId;
    private BookInfoEntity bookNum;
    private String storeCode;
    private BookStatus bookStatus;
    private boolean rentStatus;

}
