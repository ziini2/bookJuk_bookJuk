package com.itwillbs.bookjuk.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.RentEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
	
	//자정마다 연체상태 업데이트
	List<RentEntity> findByReturnDateIsNullAndRentDateBefore(Timestamp date);
	
}
