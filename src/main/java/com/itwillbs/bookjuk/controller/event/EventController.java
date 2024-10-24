package com.itwillbs.bookjuk.controller.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.service.event.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class EventController {

	private final EventService eventService;
	
	@GetMapping("/event")
	public String event() {
		log.info("EventController event()");
		return "/event/event";
	}
	
	
}
