package com.itwillbs.bookjuk.controller.userEvent;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.dto.CouponDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.CouponRepository;
import com.itwillbs.bookjuk.service.event.CouponService;
import com.itwillbs.bookjuk.service.userEvent.UserCouponService;
import com.itwillbs.bookjuk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserCouponController {
	
	private final UserCouponService userCouponService;
	
	@GetMapping("/userCoupon")
	public String coupon() {
		return "/coupon/userCoupon";
	}
	
	@PostMapping(value = "/getUserCoupon", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getUserCoupon(@RequestBody Map<String, Object> payload){
		try {
			Long userNum = SecurityUtil.getUserNum();
			UserEntity user = userCouponService.getUserByUserNum(userNum);
	        if (user == null) {
	            SecurityContextHolder.clearContext();
	            return Map.of("result", "FAIL", "message", "User not authenticated");
	        }
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
			long totalRecords = userCouponService.getAllCoupon(userNum, Pageable.unpaged()).getTotalElements();
			Page<CouponDTO> couponPage;
			
			if (searchKeyword.isEmpty()) {
				if(filter.isEmpty()) {
					couponPage = userCouponService.getAllCoupon(userNum, pageable);
				}else {
					couponPage = userCouponService.getFilteredCoupon(userNum, searchCriteria, searchKeyword, 
			    			filter, pageable);
				}			
		    } else {
		    	couponPage = userCouponService.getFilteredCoupon(userNum, searchCriteria, searchKeyword, 
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

}
