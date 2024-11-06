package com.itwillbs.bookjuk.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.RentEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
	
	//자정마다 연체상태 업데이트
	List<RentEntity> findByReturnDateIsNullAndRentDateBefore(Timestamp date);
	
	//검색
//	List<RentEntity> findByUserNameContainingOrderByRentNumDesc(String keyword);
//    List<RentEntity> findByUserIdContainingOrderByRentNumDesc(String keyword);
//    List<RentEntity> findByBookNameContainingOrderByRentNumDesc(String keyword);
	Page<RentEntity> findByUserNameContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByUserIdContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByBookNameContaining(String keyword, Pageable pageable);
	
}
