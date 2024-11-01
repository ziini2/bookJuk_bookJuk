package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.BooksEntity;

public interface BooksRepository extends JpaRepository<BooksEntity, Long> {

}
