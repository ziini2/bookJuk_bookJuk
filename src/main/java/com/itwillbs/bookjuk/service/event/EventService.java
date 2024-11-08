package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.dto.EventConditionDTO;
import com.itwillbs.bookjuk.dto.EventDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.EventConditionRepository;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import com.itwillbs.bookjuk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
	
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final EventConditionRepository eventConditionRepository;
	
	@Transactional
	public void createEvent(EventDTO eventDTO) {
		UserEntity eventManager = userRepository.findByUserNum(SecurityUtil.getUserNum());
		
		// 이벤트 시작 시간 00:00:00 설정
		Timestamp startDate = eventDTO.getStartEventDate();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);
		startDate = new Timestamp(startCal.getTimeInMillis());
		
		// 이벤트 종료 시간 00:00:00 설정
		Timestamp endDate = eventDTO.getEndEventDate();
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		endDate = new Timestamp(endCal.getTimeInMillis());
		
		EventEntity eventEntity = EventEntity.builder()
				.eventManager(eventManager)
				.eventTitle(eventDTO.getEventTitle())
				.eventContent(eventDTO.getEventContent())
				.eventStatus("시작 전")
				.eventType(eventDTO.getEventType())
				.startEventDate(startDate)
				.endEventDate(endDate)
				.eventCreationDate(new Timestamp(System.currentTimeMillis()))
				.build();
		eventRepository.save(eventEntity);
		
		for(EventConditionDTO conditionDTO : eventDTO.getEventCondition()) {
			EventConditionEntity eventConditionEntity = EventConditionEntity.builder()
					.eventId(eventEntity)
					.eventConditionType(conditionDTO.getEventConditionType())
					.eventClearReward(conditionDTO.getEventClearReward())
					.eventIsActive(false)
					.build();
			eventConditionRepository.save(eventConditionEntity);
		}
	}
	
	

}
