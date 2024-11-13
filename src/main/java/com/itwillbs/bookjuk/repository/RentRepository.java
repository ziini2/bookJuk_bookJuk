package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
    Page<RentEntity> findAll(Pageable pageable);

    Page<RentEntity> findByUser(UserEntity user, Pageable pageable);
}