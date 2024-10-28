package com.itwillbs.bookjuk.controller.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.entity.event.Notification;
import com.itwillbs.bookjuk.service.event.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class NotificationController {

	@Autowired
	private final NotificationService notiService;
	
	@MessageMapping("/sendMessage")
	public void sendMessage(Notification noti) {
		notiService.sendMessage(noti.getUserNum().getUserId(), noti.getNotiContent());
	}
	
	@GetMapping("/notification")
	public String notification() {
		log.info("NotificationController notification()");
		return "/notification/notification";
	}
	
	
}
