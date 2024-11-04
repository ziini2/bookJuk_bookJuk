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
@Table(name = "noti_check")
public class NotiCheckEntity {

	// 알림 확인 아이디
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notiCheckedId", nullable = false)
	private Long notiCheckedId;
	
	// 알림 아이디
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notiId")
    private NotificationEntity notiId;
	
	// 알림 수신인
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notiRecipient")
	private UserEntity notiRecipient;
	
	// 알림 확인 유무
	@Column(name = "notiChecked", nullable = false)
	private boolean notiChecked = false;
	
	
	
	
	
	
	
	
	
	
	
	
	
}
