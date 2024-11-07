package com.itwillbs.bookjuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventConditionDTO {
	
	// 이벤트 조건 아이디
	private Integer eventConditionId;
	
	// 이벤트 아이디
	private Integer eventId;
	
	// 이벤트 조건 유형
	private String eventConditionType;
	
	// 이벤트 달성 보상
	private String eventClearReward;
	
	// 이벤트 활성 유무
	private boolean eventIsActive = false;
	
	
	
	
	
	
	

}
