package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.books.BooksEntity;


public interface BooksRepository extends JpaRepository<BooksEntity, Long> {

	

}
