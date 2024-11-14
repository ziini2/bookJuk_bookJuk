package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, NotificationRepositoryCustom {

	// 유저 알림 페이지의 내 알림 내역
	Page<NotificationEntity> findByNotiRecipient_UserNum(Long userNum, Pageable pageable);

	boolean existsByNotiIdAndNotiRecipient_UserNum(Long notiId, Long userNum);

}
