package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.bookjuk.entity.event.EventEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Integer>, EventRepositoryCustom {

    // dashboard 사용할 메서드
    long countByStartEventDateBeforeAndEndEventDateAfter(Timestamp startEventDate, Timestamp endEventDate);
    
    // 검색
	Page<EventEntity> findByCriteriaAndFilter(String searchCriteria, 
											  String searchKeyword, 
											  List<Map<String, String>> filter, 
											  Pageable pageable);
	
	List<EventEntity> findByStartEventDateAndEventStatus(Timestamp startEventDate, String eventStatus);

    // 종료 날짜가 오늘 이전이고 상태가 "진행 중"인 이벤트 조회
    List<EventEntity> findByEndEventDateBeforeAndEventStatus(Timestamp endEventDate, String eventStatus);

    
//	@Query("SELECT e FROM EventEntity e LEFT JOIN FETCH e.eventConditions WHERE e.id = :eventId")
//    Optional<EventEntity> findEventWithConditions(@Param("eventId") Integer eventId);
}
