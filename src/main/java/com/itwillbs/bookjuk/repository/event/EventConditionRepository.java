package com.itwillbs.bookjuk.repository.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventEntity;

public interface EventConditionRepository extends JpaRepository<EventConditionEntity, Integer> {
	
	List<EventConditionEntity> findByEventId(EventEntity event);
	
}
