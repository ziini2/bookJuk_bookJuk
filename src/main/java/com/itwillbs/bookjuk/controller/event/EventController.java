package com.itwillbs.bookjuk.controller.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.bookjuk.service.event.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class EventController {

	private final EventService eventService;
	
	@GetMapping("/event")
	public String event() {
		log.info("EventController event()");
		return "/event/event";
	}
	
	
}
