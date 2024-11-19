package com.itwillbs.bookjuk.repository.event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventEntity;

public interface EventConditionRepository extends JpaRepository<EventConditionEntity, Integer> {
	
	List<EventConditionEntity> findByEventId(EventEntity event);
	
	List<EventConditionEntity> findByEventIsActiveTrueAndEventConditionTypeIn(List<String> eventConditionType);
	
	// 이벤트 시작 날짜가 오늘이고 상태가 "시작 전"인 이벤트 조회
	@Query("SELECT ec FROM EventConditionEntity ec WHERE DATE(ec.eventId.startEventDate) = :today AND ec.eventId.eventStatus = :status")
	List<EventConditionEntity> findConditionsToStart(@Param("today") LocalDate today, @Param("status") String status);
	
	// 종료 날짜가 오늘 이전이고 상태가 "진행 중"인 이벤트 조회
	@Query("SELECT ec FROM EventConditionEntity ec WHERE DATE(ec.eventId.endEventDate) < :today AND ec.eventId.eventStatus = :status")
	List<EventConditionEntity> findConditionsToEnd(@Param("today") LocalDate today, @Param("status") String status);
	
}
