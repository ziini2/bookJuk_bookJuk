package com.itwillbs.bookjuk.controller.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.service.event.CouponService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class CouponController {

	private final CouponService couponService;
	
	@GetMapping("/coupon")
	public String coupon() {
		log.info("CouponController coupon()");
		return "/coupon/coupon";
	}
	
	
}
