package com.itwillbs.bookjuk.controller.event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.dto.NotiDTO;
import com.itwillbs.bookjuk.service.event.NotificationService;
import com.itwillbs.bookjuk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NotificationController {

	private final NotificationService notiService;
	
	@GetMapping("/noti")
	public String notification() {
		return "/notification/notification";
	}
	
	@PostMapping(value = "/getNoti", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getNoti(@RequestBody Map<String, Object> payload){
		try {
			Long userNum = 0L;
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
		    if(sortColumn.equals("recipient")) {
		    	sortColumn = "notiRecipient";
		    }
		    if(sortColumn.equals("transferStatus")) {
		    	sortColumn = "notiStatus";
		    }
			int page = start / length;
			Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortColumn);
			Pageable pageable = PageRequest.of(page, length, sort);
			long totalRecords = notiService.getAllNoti(userNum, Pageable.unpaged()).getTotalElements();
			Page<NotiDTO> notiPage;
			if (searchKeyword.isEmpty()) {
				if(filter.isEmpty()) {
					notiPage = notiService.getAllNoti(userNum, pageable);
				}else {
					notiPage = notiService.getFilteredEvent(userNum, searchCriteria, searchKeyword, 
			    			filter, pageable);
				}			
		    } else {
		    	notiPage = notiService.getFilteredEvent(userNum, searchCriteria, searchKeyword, 
		    			filter, pageable);
		    }
			return Map.of(
				"result", "SUCCESS",
				"draw", draw,
		        "data", notiPage.getContent(),
		        "recordsTotal", totalRecords,
		        "recordsFiltered", notiPage.getTotalElements()
		    );
		} catch (Exception e) {
	        log.error("Failed to process getEvent request", e);
	        return Map.of(
	            "result", "FAIL",
	            "message", e.getMessage()
	        );
	    }
		
		
	}
	
	@GetMapping("/noti/{notiId}")
	public ResponseEntity<NotiDTO> getNotiDetail(@PathVariable("notiId") Long notiId) {
        NotiDTO notiDetail = notiService.getNotiDetail(notiId);                
        if (notiDetail != null) {
        	log.info("Noti Content: {}", notiDetail.getNotiContent());
            return ResponseEntity.ok(notiDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@PostMapping("/sendNotifications")
	public ResponseEntity<?> sendNotifications(@RequestBody Map<String, Object> request) {
	    try {
	    	Long userNum = SecurityUtil.getUserNum();
	    	@SuppressWarnings("unchecked")
			List<String> selectedRecipient = (List<String>) request.get("notiRecipient");
	    	List<Long> selectedRecipients = selectedRecipient.stream()
	    	        .map(Long::valueOf) // Long으로 변환
	    	        .collect(Collectors.toList());
	    	String notiContent = (String)request.get("notiContent");
	    	notiService.sendNotifications(selectedRecipients, notiContent, userNum);
	        return ResponseEntity.ok("알림 전송 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 전송 실패: " + e.getMessage());
	    }
	}
	
}
