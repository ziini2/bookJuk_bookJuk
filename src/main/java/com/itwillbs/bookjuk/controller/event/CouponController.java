package com.itwillbs.bookjuk.controller.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.bookjuk.service.event.CouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class CouponController {

	@Autowired
	private CouponService couponService;
	
	@GetMapping("/coupon")
	public String coupon() {
		return "/coupon/coupon";
	}
		
}
