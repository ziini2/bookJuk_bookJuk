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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notification")
public class Notification {

	// 알림 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notiId", nullable = false)
	private Long notiId;
	
	// 알림 수신인(유저 PK)
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum")
    private UserEntity userNum;
	
	// 알림 발신인
	@Column(name = "notiSender", length = 255, nullable = false)
	private String notiSender;
	
	// 알림 내용
	@Column(name = "notiContent", columnDefinition = "TEXT", nullable = false)
	private String notiContent;
	
	// 알림 유형(note, sms)
	@Column(name = "notiType", length = 10, nullable = false)
	private String notiType;
	
	// 알림 상태(대기, 전송, 실패)
	@Column(name = "notiStatus", length = 10, nullable = false)
	private String notiStatus;
	
	// 알림 생성 날짜
	@Column(name = "notiCreationDate", nullable = false)
	private Timestamp notiCreationDate;
	
	// 알림 전송 날짜
	@Column(name = "notiSentDate")
	private Timestamp notiSentDate;
	
	
	
	
	
	
	
	
	
	
	
}
