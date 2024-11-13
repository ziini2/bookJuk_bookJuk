package com.itwillbs.bookjuk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;


public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long> {
	
	//대여등록 책검색
	Optional<BookInfoEntity> findByIsbn(String isbn);
	
}