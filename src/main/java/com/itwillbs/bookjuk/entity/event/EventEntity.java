package com.itwillbs.bookjuk.entity.event;

import java.sql.Timestamp;

import com.itwillbs.bookjuk.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "event")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntity {

	// 이벤트 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id", nullable = false)
	private Integer eventId;
	
	// 담당자
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_manager")
    private UserEntity eventManager;
	
	// 이벤트 제목
	@Column(name = "event_title", length = 255, nullable = false)
	private String eventTitle;
	
	// 이벤트 내용
	@Column(name = "event_content", columnDefinition = "TEXT", nullable = false)
	private String eventContent;
	
	// 이벤트 상태(시작 전, 진행 중, 종료, 중지)
	@Column(name = "event_status", length = 10, nullable = false)
	private String eventStatus;
	
	// 이벤트 유형
	@Column(name = "event_type", length = 255, nullable = false)
	private String eventType;
	
	// 이벤트 시작 날짜
	@Column(name = "start_event_date", nullable = false)
	private Timestamp startEventDate;
	
	// 이벤트 종료 날짜
	@Column(name = "end_event_date", nullable = false)
	private Timestamp endEventDate;
	
	// 이벤트 생성 날짜
	@Column(name = "event_creation_date", nullable = false)
	private Timestamp eventCreationDate;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
