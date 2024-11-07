package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.UserEntity;

public interface MemberpageRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUserId(String userId);
	

}
