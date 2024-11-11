package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

}
