package com.itwillbs.bookjuk.event;

import java.sql.Time;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwillbs.bookjuk.entity.event.EventEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.EventRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class EventTest {
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;
	
	
	@Test
	public void saveData() {
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		Timestamp endTimestamp = startTime;
		for(int i = 0; i < 100; i++) {
			
			EventEntity eventEntity = EventEntity.builder()
					.eventManager(userRepository.findByUserNum(10L))
					.eventTitle("abc")
					.eventContent("abc")
					.eventStatus("시작 전")
					.eventType("쿠폰 지급")
					.startEventDate(startTime)
					.endEventDate(endTimestamp)
					.eventCreationDate(new Timestamp(System.currentTimeMillis()))
					.build();
			eventRepository.save(eventEntity);
		}
	}
	
	
	
	
	
	

}
