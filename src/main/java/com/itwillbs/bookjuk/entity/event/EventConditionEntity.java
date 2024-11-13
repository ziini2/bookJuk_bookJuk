package com.itwillbs.bookjuk.entity.event;

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
@Table(name = "event_condition")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventConditionEntity {

	// 이벤트 조건 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_condition_id", nullable = false)
	private Integer eventConditionId;
	
	// 이벤트 아이디
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private EventEntity eventId;
	
	// 이벤트 조건 유형(신규 가입자, 5회 이상 대여자, 10000원 이상 대여자 등)
	@Column(name = "event_condition_type", nullable = false)
	private String eventConditionType;
	
	// 이벤트 달성 보상(1000p, 2000p, 3000p 등)
	@Column(name = "event_clear_reward", nullable = false)
	private String eventClearReward;
	
	// 이벤트 활성 유무(기본값 false)
	@Column(name = "event_is_active", nullable = false)
	private boolean eventIsActive;
	
	// 이벤트 조건 값(3회, 5000원)
	@Column(name = "event_required_value")
	private Integer eventRequiredValue;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
