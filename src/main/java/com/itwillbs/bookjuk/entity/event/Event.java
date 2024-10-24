package com.itwillbs.bookjuk.entity.event;

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
@Table(name = "event")
@Getter
@Setter
@ToString
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventId", nullable = false)
	private int eventId;
	
	@Column(name = "eventTitle", length = 255, nullable = false)
	private String eventTitle;
	
	@Column(name = "eventContent", columnDefinition = "TEXT", nullable = false)
	private String eventContent;
	
	@Column(name = "eventStatus", length = 10, nullable = false)
	private String eventStatus;
	
	@Column(name = "eventType", length = 255, nullable = false)
	private String eventType;
	
	@Column(name = "startEvent", nullable = false)
	private Timestamp startEvent;
	
	@Column(name = "endEvent", nullable = false)
	private Timestamp endEvent;
	
	@Column(name = "eventManager", length = 50, nullable = false)
	private String eventManager;
	
	
	
	
	
	
	
	
	
	
	
}
