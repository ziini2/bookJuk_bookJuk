package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.EventEntity;

import java.sql.Timestamp;
import java.time.LocalDate;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

    // dashboard 사용할 메서드
    long countByStartEventDateBeforeAndEndEventDateAfter(Timestamp startEventDate, Timestamp endEventDate);
}
