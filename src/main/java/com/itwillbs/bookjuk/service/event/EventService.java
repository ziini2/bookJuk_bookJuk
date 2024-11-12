package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.itwillbs.bookjuk.util.EventConditionParser;
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
					.eventConditionType(EventConditionParser.parseConditionType(conditionDTO.getEventConditionType()))
					.eventClearReward(conditionDTO.getEventClearReward())
					.eventIsActive(false)
					.eventRequiredValue(EventConditionParser.parseRequiredValue(conditionDTO.getEventConditionType()))
					.build();
			eventConditionRepository.save(eventConditionEntity);
		}
	}

	public Page<EventDTO> getFilteredEvent(String searchCriteria, String searchKeyword, List<Map<String, String>> filter,
			Pageable pageable) {
		try {
	        return eventRepository.findByCriteriaAndFilter(searchCriteria, searchKeyword, 
	        		filter, pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
	        log.error("Error fetching filtered events: ", e);
	        return Page.empty(pageable); // 예외 발생 시 빈 페이지 반환
	    }
	}
	
	private EventDTO convertToDto(EventEntity eventEntity) {
		
		String eventDate = eventEntity.getStartEventDate().toLocalDateTime().toLocalDate() 
                + " ~ " 
                + eventEntity.getEndEventDate().toLocalDateTime().toLocalDate();
		
        return EventDTO.builder()
        		.eventId(eventEntity.getEventId())
        		.eventTitle(eventEntity.getEventTitle())
        		.eventType(eventEntity.getEventType())
        		.eventStatus(eventEntity.getEventStatus())
        		.eventManager(eventEntity.getEventManager().getUserName())
        		.eventDate(eventDate)
        		.eventContent(eventEntity.getEventContent())
        		.build();
    }

	public Page<EventDTO> getAllEvent(Pageable pageable) {
		try {
	        return eventRepository.findAll(pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
	        log.error("Error fetching all events: ", e);
	        return Page.empty(pageable);
	    }
	}
	
	@Scheduled(cron = "1 0 0 * * ?")// 매일 자정하고도 1초가 지났을때 동작(하루에 1번)
	@Transactional
	public void updateEventStatus() {
		LocalDate today = LocalDate.now();
        // 시작 날짜가 오늘이고 상태가 "시작 전"인 이벤트 목록을 "진행 중"으로 변경
        List<EventEntity> eventsToStart = eventRepository.findByStartEventDateAndEventStatus(today, "시작 전");
        eventsToStart.forEach(event -> event.setEventStatus("진행 중"));
        eventRepository.saveAll(eventsToStart);

        // 종료 날짜가 오늘 이전이고 상태가 "진행 중"인 이벤트 목록을 "종료"로 변경
        List<EventEntity> eventsToEnd = eventRepository.findByEndEventDateBeforeAndEventStatus(today, "진행 중");
        eventsToEnd.forEach(event -> event.setEventStatus("종료"));
        eventRepository.saveAll(eventsToEnd);
	}

	public EventDTO getEventDetail(Integer eventId) {
		EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
		String eventDate = eventEntity.getStartEventDate().toLocalDateTime().toLocalDate() 
                + " ~ " 
                + eventEntity.getEndEventDate().toLocalDateTime().toLocalDate();

        // 이벤트 조건 정보 조회
        List<EventConditionEntity> conditions = eventConditionRepository.findByEventId(eventEntity);
        List<EventConditionDTO> conditionDTOs = conditions.stream()
                .map(condition -> EventConditionDTO.builder()
                        .eventConditionType(condition.getEventConditionType())
                        .eventClearReward(condition.getEventClearReward())
                        .eventIsActive(condition.isEventIsActive())
                        .build())
                .collect(Collectors.toList());

        // EventEntity를 EventDTO로 변환
        return EventDTO.builder()
                .eventId(eventEntity.getEventId())
                .eventTitle(eventEntity.getEventTitle())
                .eventType(eventEntity.getEventType())
                .eventStatus(eventEntity.getEventStatus())
                .eventManager(eventEntity.getEventManager().getUserName())
                .eventDate(eventDate)
                .eventContent(eventEntity.getEventContent())
                .startEventDate(eventEntity.getStartEventDate())
                .endEventDate(eventEntity.getEndEventDate())
                .eventCreationDate(eventEntity.getEventCreationDate())
                .eventCondition(conditionDTOs) // 이벤트 조건 목록 추가
                .build();        
	}
	
	

}
