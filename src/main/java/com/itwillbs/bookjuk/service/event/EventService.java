package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.itwillbs.bookjuk.entity.event.CouponEntity;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventCountEntity;
import com.itwillbs.bookjuk.entity.event.EventEntity;
import com.itwillbs.bookjuk.entity.event.NotiCheckEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.CouponRepository;
import com.itwillbs.bookjuk.repository.event.EventConditionRepository;
import com.itwillbs.bookjuk.repository.event.EventCountRepository;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import com.itwillbs.bookjuk.repository.event.NotiCheckRepository;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;
import com.itwillbs.bookjuk.util.CouponUtil;
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
	private final EventCountRepository eventCountRepository;
	private final CouponRepository couponRepository;
	private final NotificationRepository notificationRepository;
	private final NotiCheckRepository notiCheckRepository;
	
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
	        return eventRepository.eventTable(searchCriteria, searchKeyword, 
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
	
	@Scheduled(cron = "10 0 0 * * ?")// 매일 자정하고도 10초가 지났을때 동작(하루에 1번)
	@Transactional
	public void updateEventStatus() {
		LocalDate today = LocalDate.now();
		
		List<EventConditionEntity> startCondition = eventConditionRepository.findConditionsToStart(today, "시작 전");
		startCondition.forEach(condition -> {
	        condition.getEventId().setEventStatus("진행 중");
	        condition.setEventIsActive(true);
	    });
		eventConditionRepository.saveAll(startCondition);
		
		List<EventConditionEntity> endCondition = eventConditionRepository.findConditionsToEnd(today, "진행 중");
		endCondition.forEach(condition -> {
	        condition.getEventId().setEventStatus("종료");
	        condition.setEventIsActive(false);
	    });
		eventConditionRepository.saveAll(endCondition);
		
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
                        .eventRequiredValue(condition.getEventRequiredValue())
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
	
	// event_count, 쿠폰, 알림, noti_check 데이터 생성 메서드
    @Transactional
    public void createEventEntitiesForUser(List<EventConditionEntity> resultList, UserEntity saveUser) {
    	    	
    	for(EventConditionEntity eventCondition : resultList) {
    		
    		String coupon;
    		
    		// 쿠폰 번호 생성(db에 중복된 쿠폰 번호가 있으면 중복 안될때까지 랜덤 문자 함수 호출) 
            do {
                coupon = CouponUtil.generateRandomCouponNum(16);
            } while (couponRepository.existsByCouponNum(coupon));
    		
    		// event_count 생성
    		EventCountEntity eventCountEntity = EventCountEntity.builder()
    				.userNum(saveUser)
    				.eventConditionId(eventCondition)
    				.eventNowCount(0)
    				.lastUpdate(new Timestamp(System.currentTimeMillis()))
    				.clearEvent(true)
    				.build();
			eventCountRepository.save(eventCountEntity);
			
			// 알림 생성
			NotificationEntity notificationEntity = NotificationEntity.builder()
					.notiRecipient(saveUser)
					.notiSender(eventCondition.getEventId().getEventManager())
					.notiContent("신규 가입자를 위한 무료 " +
								 eventCondition.getEventClearReward() +
								 " 쿠폰입니다.\n" +
								 "쿠폰 번호 : " + coupon)
					.notiType("쪽지")
					.notiStatus("성공")
					.notiCreationDate(new Timestamp(System.currentTimeMillis()))
					.notiSentDate(new Timestamp(System.currentTimeMillis()))
					.build();
			notificationRepository.save(notificationEntity);

			// 쿠폰 생성
			CouponEntity couponEntity = CouponEntity.builder()
					.eventId(eventCondition.getEventId())
					.eventConditionId(eventCondition)
					.notiId(notificationEntity)
					.userNum(saveUser)
					.couponNum(coupon)
					.couponPeriod(Timestamp.valueOf(LocalDateTime.now().plusYears(1)))
					.couponStatus("유효")
					.couponType(eventCondition.getEventClearReward())
					.build();
			couponRepository.save(couponEntity);
			
			// noti_check 생성
			NotiCheckEntity notiCheckEntity = NotiCheckEntity.builder()
					.notiId(notificationEntity)
					.notiRecipient(saveUser)
					.notiChecked(false)
					.build();
			notiCheckRepository.save(notiCheckEntity);
		}    	
    }
    
    public List<EventConditionEntity> checkEventCondition(String eventConditionType){
    	return eventConditionRepository.findByEventIsActiveTrueAndEventConditionType(eventConditionType);
    }
    
    public void checkEventForPayment(UserEntity user, EventConditionEntity condition, int count) {
    	EventCountEntity checkCountEntity = eventCountRepository.find
    	EventCountEntity countEntity = EventCountEntity.builder()
    			.userNum(user)
    			.eventConditionId(condition)
    			.eventNowCount(0)
    			.lastUpdate(new Timestamp(System.currentTimeMillis()))
    			.clearEvent(false)
    			.build();
    	int nowCount = countEntity.getEventNowCount(); // 이벤트 달성 유형에 대한 현재까지 달성한 값
    	int targetCount = condition.getEventRequiredValue(); // 이벤트 달성을 위한 목표 값
    	if(nowCount >= targetCount) {
    		countEntity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    		countEntity.setClearEvent(true);
    	}
    	eventCountRepository.save(countEntity);
    }

}
