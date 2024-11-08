package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.EventEntity;

import java.sql.Timestamp;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

    // dashboard 사용할 메서드
    long countByStartEventDateBeforeAndEndEventDateAfter(Timestamp startEventDate, Timestamp endEventDate);
    
    // 검색
	Page<EventEntity> findByCriteriaAndFilter(String searchCriteria, String searchKeyword, List<String> filter,
			Pageable pageable);
}
