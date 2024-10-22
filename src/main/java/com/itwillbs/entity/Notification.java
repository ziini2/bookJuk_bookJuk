package com.itwillbs.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notification")
@Getter
@Setter
@ToString
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notificationId", nullable = false)
	private Long notificationId;
	
	@Column(name = "notificationContent", columnDefinition = "TEXT", nullable = false)
	private String notificationContent;
	
	@Column(name = "notificationDate", nullable = false)
	private Timestamp notificationDate;
	
	@Column(name = "notificationType", length = 10, nullable = false)
	private String notificationType;
	
	
	
	
	
	
	
	
	
}
