package com.itwillbs.bookjuk.controller.event;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.service.event.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NotificationController {

	private final NotificationService notiService;
	
	@GetMapping("/{recipient}")
	@ResponseBody
	public List<NotificationEntity> getNoti(@PathVariable UserEntity recipient) {
		return notiService.getNoti(recipient);
	}
	
	@PostMapping("/send")
	@ResponseBody
	public void sendNoti(@RequestBody NotificationEntity noti) {
		notiService.sendNoti(noti);
	}
	
	@GetMapping("/noti")
	public String notification() {
		return "/notification/notification";
	}
	
	
}
