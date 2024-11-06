package com.itwillbs.bookjuk.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.RentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
	
	//자정마다 연체상태 업데이트
	List<RentEntity> findByReturnDateIsNullAndRentDateBefore(Timestamp date);
	
	//검색
	Page<RentEntity> findByUserNameContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByUserIdContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByBookNameContaining(String keyword, Pageable pageable);
    
    //membersearch 검색
    List<UserEntity> findByUserNameContaining(String keyword);
    List<UserEntity> findByUserIdContaining(String keyword);
	
}
