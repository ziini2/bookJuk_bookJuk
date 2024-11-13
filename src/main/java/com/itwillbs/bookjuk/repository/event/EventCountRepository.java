package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventCountEntity;

public interface EventCountRepository extends JpaRepository<EventConditionEntity, Long> {
	
//	boolean existsByUserNumAndEventConditionId(Long userNum, Integer eventConditionId);

	void save(EventCountEntity eventCountEntity);
	
}
