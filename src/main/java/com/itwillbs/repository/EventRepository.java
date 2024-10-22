package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
