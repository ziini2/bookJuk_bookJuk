package com.itwillbs.bookjuk.controller.event;

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

import com.itwillbs.bookjuk.dto.CouponDTO;
import com.itwillbs.bookjuk.service.event.CouponService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CouponController {

	private final CouponService couponService;
	
	@GetMapping("/coupon")
	public String coupon() {
		return "/coupon/coupon";
	}
	
	@PostMapping(value = "/getCoupon", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getCoupon(@RequestBody Map<String, Object> payload){
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
			Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortColumn);
			Pageable pageable = PageRequest.of(page, length, sort);
			long totalRecords = couponService.getAllEvent(Pageable.unpaged()).getTotalElements();
			Page<CouponDTO> couponPage;
			
			if (searchKeyword.isEmpty()) {
				if(filter.isEmpty()) {
					couponPage = couponService.getAllEvent(pageable);
				}else {
					couponPage = couponService.getFilteredEvent(searchCriteria, searchKeyword, 
			    			filter, pageable);
				}			
		    } else {
		    	couponPage = couponService.getFilteredEvent(searchCriteria, searchKeyword, 
		    			filter, pageable);
		    }
			return Map.of(
				"result", "SUCCESS",
				"draw", draw,
		        "data", couponPage.getContent(),
		        "recordsTotal", totalRecords,
		        "recordsFiltered", couponPage.getTotalElements()
		    );
		} catch (Exception e) {
	        log.error("Failed to process getEvent request", e);
	        return Map.of(
	            "result", "FAIL",
	            "message", e.getMessage()
	        );
		}		
	}
	
	@GetMapping("/coupon/{couponId}")
	public ResponseEntity<CouponDTO> getCouponDetail(@PathVariable("couponId") Long couponId) {
		CouponDTO couponDetail = couponService.getCouponDetail(couponId);                
        if (couponDetail != null) {
        	log.info("Coupon Content: {}", couponDetail.getCouponStatus());
            return ResponseEntity.ok(couponDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
		
}
