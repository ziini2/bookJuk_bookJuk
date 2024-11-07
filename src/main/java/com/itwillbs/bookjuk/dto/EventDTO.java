package com.itwillbs.bookjuk.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
	
	// 이벤트 아이디
	private Integer eventId;
	
	// 담당자 아이디
	private Long userNum;
	
	// 이벤트 제목
	private String eventTitle;
	
	// 이벤트 내용
	private String eventContent;
	
	// 이벤트 상태
	private String eventStatus = "시작 전";
	
	// 이벤트 유형
	private String eventType;
	
	// 이벤트 시작 날짜
	private Timestamp startEventDate;
	
	// 이벤트 종료 날짜
	private Timestamp endEventDate;
	
	// 이벤트 생성 날짜
	private Timestamp eventCreationDate;
	
	// 이벤트 조건
	private List<EventConditionDTO> eventCondition;
	
	// 담당자 이름
	private String eventManager;

}
