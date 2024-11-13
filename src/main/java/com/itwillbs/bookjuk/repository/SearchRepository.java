package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.UserEntity;

public interface SearchRepository extends JpaRepository<UserEntity, Long> {
    
    //membersearch 검색
	List<UserEntity> findByUserNameContainingIgnoreCase(String userName);
    List<UserEntity> findByUserIdContainingIgnoreCase(String userId);
	
}
