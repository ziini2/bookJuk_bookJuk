package com.itwillbs.bookjuk.controller.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Map<String, Object> getEvent(@RequestBody Map<String, Object> payload){
		String searchCriteria = (String) payload.get("searchCriteria");
	    String searchKeyword = (String) payload.get("searchKeyword");
	    @SuppressWarnings("unchecked")
		List<Map<String, String>> filter = (List<Map<String, String>>) payload.get("filter");
	    Integer start = (Integer) payload.get("start");
	    Integer length = (Integer) payload.get("length");
	    String sortColumn = (String) payload.get("sortColumn");
	    String sortDirection = (String) payload.get("sortDirection");
		int page = start / length;
		Pageable pageable = PageRequest.of(page, length);
		long totalRecords = eventService.getAllEvent(Pageable.unpaged()).getTotalElements();
		Page<EventDTO> eventPage;
		if ((searchCriteria == null || searchCriteria.isEmpty()) && 
			(searchKeyword == null ||searchKeyword.isEmpty())) {
			eventPage = eventService.getAllEvent(pageable);
	    } else {
	    	eventPage = eventService.getFilteredEvent(searchCriteria, searchKeyword, 
	    			filter, pageable, sortColumn, sortDirection);
	    }
		return Map.of(
	        "data", eventPage.getContent(),
	        "recordsTotal", totalRecords,
	        "recordsFiltered", eventPage.getTotalElements()
	    );
	}
	
	
	
	
}
