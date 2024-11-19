package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    // coupon 생성
    public void createCoupon(EventConditionEntity eventCondition, NotificationEntity notificationEntity, UserEntity user) {
    	String coupon;
		// 쿠폰 번호 생성(db에 중복된 쿠폰 번호가 있으면 중복 안될때까지 랜덤 문자 함수 호출) 
        do {
            coupon = CouponUtil.generateRandomCouponNum(16);
        } while (couponRepository.existsByCouponNum(coupon));
    	CouponEntity couponEntity = CouponEntity.builder()
				.eventId(eventCondition.getEventId())
				.eventConditionId(eventCondition)
				.notiId(notificationEntity)
				.userNum(user)
				.couponNum(coupon)
				.couponPeriod(Timestamp.valueOf(LocalDateTime.now().plusYears(1)))
				.couponStatus("유효")
				.couponType(eventCondition.getEventClearReward())
				.build();
		couponRepository.save(couponEntity);
    }
    
    // noti_check 생성
    public void createNotiCheck(UserEntity user, NotificationEntity notificationEntity) {
    	NotiCheckEntity notiCheckEntity = NotiCheckEntity.builder()
				.notiId(notificationEntity)
				.notiRecipient(user)
				.notiChecked(false)
				.build();
		notiCheckRepository.save(notiCheckEntity);
    }
    
    // 이벤트 컨디션 타입에 따른 이벤트 활성값 true 조회
    public List<EventConditionEntity> checkEventCondition(List<String> eventConditionType){
    	return eventConditionRepository.findByEventIsActiveTrueAndEventConditionTypeIn(eventConditionType);
    }
    // 오버로딩 : checkEventCondition(String)
    public List<EventConditionEntity> checkEventCondition(String eventConditionType) {        
        return checkEventCondition(Collections.singletonList(eventConditionType));
    }
    
    // 유저 엔티티와 이벤트 컨디션 엔티티에 따른 이벤트 카운트 조회
    public EventCountEntity checkEventCount(UserEntity user, EventConditionEntity condition) {
    	return eventCountRepository.findByUserNumAndEventConditionId(user, condition);
    }
    
    @Transactional
    public void checkEventForPayment(UserEntity user, int numberOfRental, int rentalAmount) {
    	// 조회할 이벤트 컨디션 설정
    	List<String> eventConditionType = List.of("대여 횟수", "대여 금액");
    	// 이벤트 컨디션 타입에 따른 이벤트 활성값 true 조회
    	List<EventConditionEntity> eventConditionEntities = checkEventCondition(eventConditionType);
    	// 없으면 함수 종료
    	if(eventConditionEntities.isEmpty()) { return; }
    	
    	// 유저 엔티티와 이벤트 컨디션에 따른 이벤트 카운트 조회
    	for (EventConditionEntity eventConditionEntity : eventConditionEntities) {
    		EventCountEntity eventCountEntity = checkEventCount(user, eventConditionEntity);
    		int countValue = "대여 횟수".equals(eventConditionEntity.getEventConditionType()) ? numberOfRental : rentalAmount;
    		boolean isClear = false;
    		if (eventCountEntity != null) {
    			// 기존 값 업데이트
    	        eventCountEntity.setEventNowCount(eventCountEntity.getEventNowCount() + countValue);
    	        isClear = eventCountEntity.getEventNowCount() >= eventConditionEntity.getEventRequiredValue();
    	        eventCountEntity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    	        eventCountEntity.setClearEvent(isClear);
    	        eventCountRepository.save(eventCountEntity);
    	    } else {
    	    	// 새로운 이벤트 카운트 생성
    	        isClear = countValue >= eventConditionEntity.getEventRequiredValue();
    	        EventCountEntity newEventCountEntity = EventCountEntity.builder()
    	            .userNum(user)
    	            .eventConditionId(eventConditionEntity)
    	            .eventNowCount(countValue)
    	            .lastUpdate(new Timestamp(System.currentTimeMillis()))
    	            .clearEvent(isClear)
    	            .build();
    	        eventCountRepository.save(newEventCountEntity);
    	    }
    		if(isClear) {
    			String coupon;
        		// 쿠폰 번호 생성(db에 중복된 쿠폰 번호가 있으면 중복 안될때까지 랜덤 문자 함수 호출) 
    	        do {
    	            coupon = CouponUtil.generateRandomCouponNum(16);
    	        } while (couponRepository.existsByCouponNum(coupon));
    			NotificationEntity notificationEntity = NotificationEntity.builder()
    					.notiRecipient(user)
    					.notiSender(eventConditionEntity.getEventId().getEventManager())
    					.notiContent("대여 이벤트 기간동안 발행되는 " +
    							eventConditionEntity.getEventClearReward() +
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
    					.eventId(eventConditionEntity.getEventId())
    					.eventConditionId(eventConditionEntity)
    					.notiId(notificationEntity)
    					.userNum(user)
    					.couponNum(coupon)
    					.couponPeriod(Timestamp.valueOf(LocalDateTime.now().plusYears(1)))
    					.couponStatus("유효")
    					.couponType(eventConditionEntity.getEventClearReward())
    					.build();
    			couponRepository.save(couponEntity);
    			
    			// noti_check 생성
    			NotiCheckEntity notiCheckEntity = NotiCheckEntity.builder()
    					.notiId(notificationEntity)
    					.notiRecipient(user)
    					.notiChecked(false)
    					.build();
    			notiCheckRepository.save(notiCheckEntity);
    		}
    	}
    }
}
