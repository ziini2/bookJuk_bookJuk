package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;


public interface UserContentRepository extends JpaRepository<UserContentEntity, Long> {
    //UserEntity의 userNum을 기준으로 UserContentEntity를 찾는 메서드
	  UserContentEntity findByUserNum(Long userNum);
}