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
public class NotiDTO {
	
	// 이벤트 아이디
	private Long notiId;
	
	// 수신 아이디
	private Long notiRecipient;
	
	// 발신 아이디
	private Long notiSender;
	
	// 알림 내용
	private String notiContent;
	
	// 알림 유형(쪽지, SMS)
	private String notiType;
	
	// 전송 상태(대기, 전송, 실패)
	private String notiStatus;
	
	// 알림 생성 날짜
	private Timestamp notiCreationDate;
	
	// 알림 전송 날짜
	private Timestamp notiSentDate;
	
	// 수신 아이디에서 아이디 추출
	private String recipient;
	
	// 발신 아이디에서 이름 추출
	private String sender;
	
	// 알림 체크 아이디
	private Long notiCheckedId;
	
	// 알림 확인 유무
	private String notiChecked;
	
	private Integer count;
	
	public static class NotiDTOBuilder {
		public NotiDTOBuilder notiChecked(Boolean notiChecked) {
			this.notiChecked = notiChecked != null && notiChecked ? "읽음" : "읽지 않음";
			return this;
		}
	}

}
