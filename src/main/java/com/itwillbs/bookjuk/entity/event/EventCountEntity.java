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
@Table(name = "event_count")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCountEntity {

	// 이벤트 카운트 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_count_id", nullable = false)
	private Long eventCountId;
	
	// 유저 번호
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num")
    private UserEntity userNum;
	
	// 이벤트 조건 아이디
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_condition_id")
	private EventConditionEntity eventConditionId;
	
	// 이벤트 현재 값
	@Column(name = "event_now_count")
	private Integer eventNowCount;
	
	// 마지막 업데이트
	@Column(name = "last_update")
	private Timestamp lastUpdate;
	
	// 이벤트 달성 유무
	@Column(name = "clear_event", nullable = false)
	private boolean clearEvent;
	

	
	
}
