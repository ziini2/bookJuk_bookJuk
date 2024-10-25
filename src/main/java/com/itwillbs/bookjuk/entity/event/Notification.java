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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notificationId", nullable = false)
	private Long notificationId;
	
	// 유저 PK
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum")
    private UserEntity userNum;
	
	@Column(name = "notificationContent", columnDefinition = "TEXT", nullable = false)
	private String notificationContent;
	
	@Column(name = "notificationDate", nullable = false)
	private Timestamp notificationDate;
	
	@Column(name = "notificationType", length = 10, nullable = false)
	private String notificationType;
	
	
	
	
	
	
	
	
	
}
