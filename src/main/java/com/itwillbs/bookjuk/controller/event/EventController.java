package com.itwillbs.bookjuk.controller.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.dto.EventDTO;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.service.event.EventService;
import com.itwillbs.bookjuk.util.SecurityUtil;

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
		return "/event/event";
	}

	@PostMapping
	@ResponseBody
	public Map<String, String> createEvent(@RequestBody EventDTO eventDTO) {
		Map<String, String> response = new HashMap<>();
		
		return response;
	}
	
	
}
