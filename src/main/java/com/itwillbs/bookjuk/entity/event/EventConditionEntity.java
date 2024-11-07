package com.itwillbs.bookjuk.entity.event;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "event_condition")
public class EventConditionEntity {

	// 이벤트 조건 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventConditionId", nullable = false)
	private int eventConditionId;
	
	// 이벤트 아이디
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private EventEntity eventId;
	
	// 이벤트 조건 유형(신규 가입자, 5회 이상 대여자, 10000원 이상 대여자 등)
	@Column(name = "eventConditionType", nullable = false)
	private String eventConditionType;
	
	// 이벤트 달성 보상(1000p, 2000p, 3000p 등)
	@Column(name = "eventClearReward", nullable = false)
	private String eventClearReward;
	
	// 이벤트 활성 유무(기본값 false)
	@Column(name = "eventIsActive", nullable = false)
	private boolean eventIsActive = false;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
