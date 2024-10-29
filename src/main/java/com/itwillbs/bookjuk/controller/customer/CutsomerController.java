package com.itwillbs.bookjuk.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CutsomerController {
	
	@GetMapping("/admin/store/store_list")
	public String storeList() {
		return "customer/store";
	}
	
	@GetMapping("/admin/store/store_info")
	public String storeInfo() {
		return "customer/store_info";
	}
	
	@GetMapping("/admin/user/user_list")
	public String userList() {
		return "customer/user";
	}
	
	@GetMapping("/admin/user/user_info")
	public String userInfo() {
		return "customer/user_info";
	}
}
