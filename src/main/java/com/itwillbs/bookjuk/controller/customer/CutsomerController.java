package com.itwillbs.bookjuk.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CutsomerController {
	
	@GetMapping("/store/store_list")
	public String store() {
		return "customer/store";
	}
	
}
