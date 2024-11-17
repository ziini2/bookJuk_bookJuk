package com.itwillbs.bookjuk.util;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;

public class BookConverter {

    public static BookDTO convertToBookDTO(BooksEntity booksEntity) {
        BookInfoEntity bookInfo = booksEntity.getBookInfoEntity();
        return BookDTO.builder()
                .booksId(booksEntity.getBooksId())
                .isbn(bookInfo.getIsbn())
                .bookImage(bookInfo.getBookImage())
                .bookName(bookInfo.getBookName())
                .author(bookInfo.getAuthor())
                .publish(bookInfo.getPublish())
                .publishDate(bookInfo.getPublishDate())
                .story(bookInfo.getStory())
                .genreId(bookInfo.getGenre().getGenreId())
                .storeCode(booksEntity.getStoreEntity().getStoreCode())
                .bookStatus(booksEntity.getBookStatus())
                .rentMoney(bookInfo.getRentMoney())
                .rentCount(bookInfo.getRentCount())
                .rentStatus(booksEntity.getRentStatus())
                .bookDate(bookInfo.getBookDate())
                .build();
    }

    public static BookInfoEntity convertToBookInfoEntity(BookDTO bookDTO, GenreEntity genreEntity) {
        return BookInfoEntity.builder()
                .bookName(bookDTO.getBookName())
                .author(bookDTO.getAuthor())
                .publish(bookDTO.getPublish())
                .story(bookDTO.getStory())
                .genre(genreEntity)
                .isbn(bookDTO.getIsbn())
                .bookImage(bookDTO.getBookImage())
                .publishDate(bookDTO.getPublishDate())
                .rentMoney(bookDTO.getRentMoney())
                .rentCount(bookDTO.getRentCount())
                .build();
    }

    public static BooksEntity convertToBooksEntity(BookDTO bookDTO, BookInfoEntity bookInfoEntity, StoreEntity storeEntity) {
        return BooksEntity.builder()
                .bookStatus(bookDTO.getBookStatus())
                .rentStatus(true)
                .storeEntity(storeEntity)
                .bookInfoEntity(bookInfoEntity)
                .build();
    }
}
