package com.itwillbs.bookjuk.repository.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itwillbs.bookjuk.entity.event.EventEntity;

public interface EventRepositoryCustom {
	
	// 이벤트 관리 페이지 리스트 출력
	Page<EventEntity> eventTable(String searchCriteria, 
										  String searchKeyword, 
										  List<Map<String, String>> filter, 
										  Pageable pageable);
}
