package com.itwillbs.bookjuk.controller.userEvent;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/user")
public class UserEventController {

	private final EventService eventService;
	
	@GetMapping("/event")
	public String event() {
		return "/event/userEvent";
	}
	
	@PostMapping(value = "/getUserEvent", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getEvent(@RequestBody Map<String, Object> payload){
		try {
			String searchCriteria = (String) payload.get("searchCriteria");
		    String searchKeyword = (String) payload.get("searchKeyword");
		    @SuppressWarnings("unchecked")
			List<Map<String, String>> filter = (List<Map<String, String>>) payload.get("filter");
		    filter.forEach(map -> log.info("Filter item: {}", map));
		    Integer start = (Integer) payload.get("start");
		    Integer length = (Integer) payload.get("length");
		    Integer draw = (Integer) payload.get("draw");
		    String sortColumn = (String) payload.get("sortColumn");
		    String sortDirection = (String) payload.get("sortDirection");
			int page = start / length;
			if(sortColumn.equals("eventDate")) {
				sortColumn = "startEventDate";
			}
			Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortColumn);
			Pageable pageable = PageRequest.of(page, length, sort);
			long totalRecords = eventService.getAllEvent(Pageable.unpaged()).getTotalElements();
			Page<EventDTO> eventPage;
			
			if (searchKeyword.isEmpty()) {
				if(filter.isEmpty()) {
					eventPage = eventService.getAllEvent(pageable);
				}else {
					eventPage = eventService.getFilteredEvent(searchCriteria, searchKeyword, 
			    			filter, pageable);
				}			
		    } else {
		    	eventPage = eventService.getFilteredEvent(searchCriteria, searchKeyword, 
		    			filter, pageable);
		    }
			return Map.of(
				"result", "SUCCESS",
				"draw", draw,
		        "data", eventPage.getContent(),
		        "recordsTotal", totalRecords,
		        "recordsFiltered", eventPage.getTotalElements()
		    );
		} catch (Exception e) {
	        log.error("Failed to process getEvent request", e);
	        return Map.of(
	            "result", "FAIL",
	            "message", e.getMessage()
	        );
	    }
	}

	@GetMapping("/event/{eventId}")
	public ResponseEntity<EventDTO> getEventDetail(@PathVariable("eventId") Integer eventId) {
        EventDTO eventDetail = eventService.getEventDetail(eventId);                
        if (eventDetail != null) {
        	log.info("Event Condition: {}", eventDetail.getEventCondition());
            return ResponseEntity.ok(eventDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	
}
