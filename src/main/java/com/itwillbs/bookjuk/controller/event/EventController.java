package com.itwillbs.bookjuk.controller.event;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.dto.EventDTO;
import com.itwillbs.bookjuk.service.event.EventService;

import lombok.RequiredArgsConstructor;
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

	@PostMapping("/eventCreate")
	@ResponseBody
	public Map<String, String> createEvent(@RequestBody EventDTO eventDTO) {
		try {
			eventService.createEvent(eventDTO);
			return Map.of("result","success");
		}catch(Exception e) {
			return Map.of("result","error");
		}
	}
	
	@PostMapping("/getEvent")
	@ResponseBody
	public Map<String, String> getEvent(
			@RequestParam(required = false) String searchCriteria,
			@RequestParam(required = false) String searchKeyword,
			@RequestParam(required = false) List<String> filter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size){
		return Map.of("1", "2");
	}
	
	
	
	
}
