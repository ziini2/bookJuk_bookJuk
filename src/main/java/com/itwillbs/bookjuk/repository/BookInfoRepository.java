package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;


public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long> {

	
}