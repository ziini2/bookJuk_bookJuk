package com.itwillbs.bookjuk.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CutsomerController {
	
	@GetMapping("/store/store_list")
	public String storeList() {
		return "customer/store";
	}
	
	@GetMapping("/store/store_info")
	public String storeInfo() {
		return "customer/store_info";
	}
	
	@GetMapping("/user/user_list")
	public String userList() {
		return "customer/user";
	}
	
	@GetMapping("/user/user_info")
	public String userInfo() {
		return "customer/user_info";
	}
}
