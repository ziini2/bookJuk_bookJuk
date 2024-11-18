package com.itwillbs.bookjuk.util;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;

public class BookConverter {

    public static BookDTO convertToBookDTO(BooksEntity booksEntity) {
        BookInfoEntity bookInfo = booksEntity.getBookInfoEntity(); // 수정된 메서드
        return BookDTO.builder()
                .booksId(booksEntity.getBooksId()) // BooksEntity의 booksId
                .isbn(bookInfo.getIsbn()) // BookInfoEntity의 ISBN
                .bookImage(bookInfo.getBookImage()) // BookInfoEntity의 이미지
                .bookName(bookInfo.getBookName()) // BookInfoEntity의 책 이름
                .author(bookInfo.getAuthor()) // BookInfoEntity의 저자
                .publish(bookInfo.getPublish()) // BookInfoEntity의 출판사
                .publishDate(bookInfo.getPublishDate()) // BookInfoEntity의 출판일
                .story(bookInfo.getStory()) // BookInfoEntity의 책 소개
                .genreId(bookInfo.getGenre().getGenreId()) // BookInfoEntity의 장르 ID
                .storeCode(booksEntity.getStoreEntity().getStoreCode()) // StoreEntity의 storeCode
                .bookStatus(booksEntity.getBookStatus()) // BooksEntity의 상태
                .rentMoney(bookInfo.getRentMoney()) // BookInfoEntity의 대여 금액
                .rentCount(bookInfo.getRentCount()) // BookInfoEntity의 대여 횟수
                .rentStatus(booksEntity.getRentStatus()) // BooksEntity의 대여 상태
                .build();
    }

    public static BookInfoEntity convertToBookInfoEntity(BookDTO bookDTO, GenreEntity genreEntity) {
        return BookInfoEntity.builder()
                .bookName(bookDTO.getBookName()) // DTO에서 책 이름
                .author(bookDTO.getAuthor()) // DTO에서 저자
                .publish(bookDTO.getPublish()) // DTO에서 출판사
                .story(bookDTO.getStory()) // DTO에서 책 소개
                .genre(genreEntity) // 연결된 장르
                .isbn(bookDTO.getIsbn()) // DTO에서 ISBN
                .bookImage(bookDTO.getBookImage()) // DTO에서 이미지
                .publishDate(bookDTO.getPublishDate()) // DTO에서 출판일
                .rentMoney(bookDTO.getRentMoney()) // DTO에서 대여 금액
                .rentCount(bookDTO.getRentCount()) // DTO에서 대여 횟수
                .bookDate(bookDTO.getBookDate())
                .build();
    }

    public static BooksEntity convertToBooksEntity(BookDTO bookDTO, BookInfoEntity bookInfoEntity, StoreEntity storeEntity) {
        return BooksEntity.builder()
                .bookStatus(bookDTO.getBookStatus()) // DTO에서 상태
                .rentStatus(true) // 대여 상태 (기본값 true)
                .storeEntity(storeEntity) // 연결된 StoreEntity
                .bookInfoEntity(bookInfoEntity) // 연결된 BookInfoEntity
                .build();
    }
}
