package com.itwillbs.bookjuk.repository.event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.bookjuk.dto.NotiDTO;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, NotificationRepositoryCustom {

	// 유저 알림 페이지의 내 알림 내역
	Page<NotificationEntity> findByNotiRecipient_UserNum(Long userNum, Pageable pageable);

	boolean existsByNotiIdAndNotiRecipient_UserNum(Long notiId, Long userNum);
	
	// 알림 수신함
	@Query(
//	    value = "SELECT new com.itwillbs.bookjuk.dto.NotiDTO(" +
//	            "n.notiId, n.notiContent, n.notiSentDate, " +
//	            "nc.notiCheckedId, nc.notiChecked, nc.notiRecipient) " +
//	            "FROM NotificationEntity n " +
//	            "JOIN NotiCheckEntity nc ON n.notiId = nc.notiId " +
//	            "WHERE nc.notiRecipient = :userNum",
		value = "SELECT new com.itwillbs.bookjuk.dto.NotiDTO(" +
	            "n.notiId, n.notiContent, n.notiSentDate, " +
	            "nc.notiCheckedId, nc.notiChecked, nc.notiRecipient) " +
	            "FROM NotiCheckEntity nc" +
	            "JOIN NotificationEntity n ON n.notiId = nc.notiId " +
	            "WHERE nc.notiRecipient = :userNum",
	    countQuery = "SELECT COUNT(*) FROM NotiCheckEntity nc" +
	                 "JOIN NotificationEntity n ON n.notiId = nc.notiId " +
	                 "WHERE nc.notiRecipient = :userNum"
	)
	Page<NotiDTO> abcdefg(@Param("userNum") Long userNum, Pageable pageable);

}
