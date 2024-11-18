package com.itwillbs.bookjuk.controller.userEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.itwillbs.bookjuk.dto.CouponDTO;
import com.itwillbs.bookjuk.service.event.CouponService;
import com.itwillbs.bookjuk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCouponController {
	
	private final CouponService couponService;
	
	@GetMapping("/coupon")
	public String coupon() {
		return "/coupon/userCoupon";
	}
	
	@PostMapping(value = "/getUserCoupon", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getUserCoupon(@RequestBody Map<String, Object> payload){
		try {
			Long userNum = SecurityUtil.getUserNum();
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
		    if(sortColumn.equals("userId")) {
		    	sortColumn = "userNum";
		    }
		    if(sortColumn.equals("eventTitle")) {
		    	sortColumn = "eventId";
		    }
			int page = start / length;
			Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortColumn);
			Pageable pageable = PageRequest.of(page, length, sort);
			long totalRecords = couponService.getAllCoupon(userNum, Pageable.unpaged()).getTotalElements();
			Page<CouponDTO> couponPage;
			
			if (searchKeyword.isEmpty()) {
				if(filter.isEmpty()) {
					couponPage = couponService.getAllCoupon(userNum, pageable);
				}else {
					couponPage = couponService.getFilteredCoupon(userNum, searchCriteria, searchKeyword, 
			    			filter, pageable);
				}			
		    } else {
		    	couponPage = couponService.getFilteredCoupon(userNum, searchCriteria, searchKeyword, 
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
	public ResponseEntity<?> getCouponDetail(@PathVariable("couponId") Long couponId) {
		Long userNum = SecurityUtil.getUserNum();
	    boolean userCheck = couponService.getCouponByIdAndUserNum(couponId, userNum);
	    if (!userCheck) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                             .body(Map.of("result", "FAIL", "message", "Access denied"));
	    }
		CouponDTO couponDetail = couponService.getCouponDetail(couponId);                
        if (couponDetail != null) {
        	log.info("Coupon Content: {}", couponDetail.getCouponStatus());
            return ResponseEntity.ok(couponDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	// 쿠폰 사용
	@PostMapping("/couponUse")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> couponUse(
			@RequestBody Map<String, String> request){
		Long userNum = SecurityUtil.getUserNum();
		String couponNum = request.get("couponCode");
		boolean isApplied = couponService.useCoupon(userNum, couponNum);
		Map<String, Object> response = new HashMap<>();
	    if (isApplied) {
	        response.put("success", true);
	        response.put("message", "쿠폰이 성공적으로 사용되었습니다.");
	    } else {
	        response.put("success", false);
	        response.put("message", "유효하지 않은 쿠폰입니다.");
	    }
	    return ResponseEntity.ok(response);
	}
}
