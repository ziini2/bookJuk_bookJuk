package com.itwillbs.bookjuk.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class NotificationService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	public void sendMessage(String recipient, String content) {
		messagingTemplate.convertAndSendToUser(recipient, "/topic/messages", content);
	}

}
