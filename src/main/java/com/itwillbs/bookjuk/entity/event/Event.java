package com.itwillbs.bookjuk.entity.event;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "event")
@Getter
@Setter
@ToString
public class Event {

	// 이벤트 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventId", nullable = false)
	private Integer eventId;
	
	// 이벤트 제목
	@Column(name = "eventTitle", length = 255, nullable = false)
	private String eventTitle;
	
	// 이벤트 내용
	@Column(name = "eventContent", columnDefinition = "TEXT", nullable = false)
	private String eventContent;
	
	// 이벤트 상태(시작 전, 진행 중, 종료, 중지)
	@Column(name = "eventStatus", length = 10, nullable = false)
	private String eventStatus;
	
	// 이벤트 유형
	@Column(name = "eventType", length = 255, nullable = false)
	private String eventType;
	
	// 이벤트 시작 날짜
	@Column(name = "startEventDate", nullable = false)
	private Timestamp startEventDate;
	
	// 이벤트 종료 날짜
	@Column(name = "endEventDate", nullable = false)
	private Timestamp endEventDate;
	
	// 이벤트 생성 날짜
	@Column(name = "endCreationDate", nullable = false)
	private Timestamp endCreationDate;
	
	// 담당자
	@Column(name = "eventManager", length = 255, nullable = false)
	private String eventManager;
	
	
	
	
	
	
	
	
	
	
	
}
