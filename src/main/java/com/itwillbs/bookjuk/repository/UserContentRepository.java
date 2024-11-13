package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.bookjuk.entity.UserContentEntity;



public interface UserContentRepository extends JpaRepository<UserContentEntity, Long> {
//    //UserContentEntity의 memberNum을 기준으로 UserContentEntity를 찾는 메서드
	UserContentEntity findByMemberNum(Long memberNum);
	
	 // memberNum을 기준으로 userNum을 조회
//	@Query("SELECT uce.userEntity.userNum FROM UserContentEntity uce WHERE uce.memberNum = :memberNum")
//	Long findUserNumByMemberNum(Long memberNum);
	@Query("SELECT uce.userEntity.userNum FROM UserContentEntity uce WHERE uce.memberNum = :memberNum")
	Long findUserNumByMemberNum(@Param("memberNum") Long memberNum);

	//유저 포인트 조회용 (유저메인페이지)
	UserContentEntity findByUserEntity_UserNum(Long userNum);
	 
}


