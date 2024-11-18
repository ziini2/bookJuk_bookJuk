package com.itwillbs.bookjuk.repository.event;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.NotiCheckEntity;

public interface NotiCheckRepository extends JpaRepository<NotiCheckEntity, Long>{

	// 유저 넘버를 통해 알림 체크 테이블 조회
	Page<NotiCheckEntity> findByNotiRecipient_UserNum(Long userNum, Pageable pageable);
	
	Optional<NotiCheckEntity> findByNotiId_NotiId(Long notiId);
	
}
