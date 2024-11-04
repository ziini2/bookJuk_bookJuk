package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

}
