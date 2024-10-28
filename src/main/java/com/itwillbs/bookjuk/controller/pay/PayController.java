package com.itwillbs.bookjuk.controller.pay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayController {
	
	@GetMapping("/pay_list")
	public String payList() {

		return "/pay/pay_list";
	}
	
	@GetMapping("/refund")
	public String refund() {

		return "/pay/refund";
	}
	
}
