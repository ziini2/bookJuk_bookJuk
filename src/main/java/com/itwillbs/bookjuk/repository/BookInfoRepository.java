package com.itwillbs.bookjuk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;

public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long> {

    // 대여등록 책검색
    Optional<BookInfoEntity> findByIsbn(String isbn);

    @Query("SELECT bi FROM BookInfoEntity bi WHERE bi.bookName LIKE %:search%")
    Page<BookInfoEntity> findByBookNameContaining(@Param("search") String search, Pageable pageable);

    Optional<List<BookInfoEntity>> findAllByBookNameContaining(String keyword);

}
