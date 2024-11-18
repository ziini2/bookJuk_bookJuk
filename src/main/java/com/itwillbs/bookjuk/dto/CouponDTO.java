package com.itwillbs.bookjuk.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
	
	// 쿠폰 아이디
	private Long couponId;
	
	// 이벤트 아이디
	private Integer eventId;
	
	// 이벤트 조건 아이디
	private Integer eventConditionId;
	
	// 알림 아이디
	private Long notiId;
	
	// 유저 아이디 PK
	private Long userNum;
	
	// 쿠폰 번호
	private String couponNum;

	// 쿠폰 유효기간(1년)
	private Timestamp couponPeriod;
	
	// 쿠폰 상태
	private String couponStatus;
	
	// 쿠폰 종류
	private String couponType;
	
	// 이벤트 제목
	private String eventTitle;
	
	// 유저 아이디
	private String userId;


}
