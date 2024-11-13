package com.itwillbs.bookjuk.controller.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		
}
