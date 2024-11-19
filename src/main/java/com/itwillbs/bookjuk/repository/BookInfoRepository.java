package com.itwillbs.bookjuk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;

public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long> {

    // 대여등록 책검색
    Optional<BookInfoEntity> findByIsbn(String isbn);

    @Query("SELECT bi FROM BookInfoEntity bi WHERE bi.bookName LIKE %:search%")
    Page<BookInfoEntity> findByBookNameContaining(@Param("search") String search, Pageable pageable);

    Optional<List<BookInfoEntity>> findAllByBookNameContaining(String keyword);

    //책 대여카운트 별
    @Query("SELECT b FROM BookInfoEntity b WHERE b.bookImage IS NOT NULL ORDER BY b.rentCount DESC")
    List<BookInfoEntity> findByBooksByRentCount(Pageable pageable);

    //도서정보 수정
    @Modifying
    @Transactional
    @Query("UPDATE BooksEntity b " +
           "SET b.bookStatus = :bookStatus, b.rentStatus = :rentStatus, b.bookUpdate = CURRENT_TIMESTAMP " +
           "WHERE b.booksId = :booksId")
   void oneBookUpdate(@Param("bookStatus") BookStatus bookStatus, @Param("rentStatus") Boolean rentStatus, @Param("booksId") Long booksId);

}