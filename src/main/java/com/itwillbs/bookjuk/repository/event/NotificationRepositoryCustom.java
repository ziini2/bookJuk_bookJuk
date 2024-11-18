package com.itwillbs.bookjuk.repository.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.event.NotificationEntity;

public interface NotificationRepositoryCustom {
	
	// 알림 관리 페이지 리스트 출력
	Page<NotificationEntity> notiTable(Long userNum, 
									   String searchCriteria, 
									   String searchKeyword, 
									   List<Map<String, String>> filter, 
									   Pageable pageable);
}
