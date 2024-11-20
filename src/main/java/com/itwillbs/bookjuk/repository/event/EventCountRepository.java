package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventCountEntity;

public interface EventCountRepository extends JpaRepository<EventCountEntity, Long> {
	
//	boolean existsByUserNumAndEventConditionId(Long userNum, Integer eventConditionId);

//	void save(EventCountEntity eventCountEntity);
	
	// userNum, event_condition_id를 통해 이벤트 카운트 조회
	EventCountEntity findByUserNumAndEventConditionIdAndClearEventFalse(UserEntity userNum, EventConditionEntity eventConditionId);
	
	boolean existsByUserNumAndEventConditionIdAndClearEventTrue(UserEntity userNum, EventConditionEntity eventConditionId);
	
	boolean existsByEventCountIdAndClearEventFalse(Long eventCountId);
	
	boolean existsByEventConditionIdAndClearEventTrue(EventConditionEntity eventConditionEntity);
	
	
	
	
	
}
