package com.itwillbs.bookjuk.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotiCheckDTO {
	
	// 알림 체크 아이디
	private Long notiCheckedId;
	
	// 알림 아이디
	private Long notiId;
	
	// 수신 아이디
	private Long notiRecipient;
	
	// 알림 내용
	private String notiContent;

	// 알림 전송 날짜
	private Timestamp notiSentDate;

	// 발신 아이디에서 이름 추출
	private String sender;
	
	// 알림 확인 여부
	private boolean notiChecked;
	
	// 알림 확인 여부를 스트링으로 변환
	private String readCheck;

}
