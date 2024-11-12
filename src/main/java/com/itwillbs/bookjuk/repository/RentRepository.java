package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RentRepository extends JpaRepository<RentEntity, Long> {

}