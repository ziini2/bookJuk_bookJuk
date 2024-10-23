package com.itwillbs.bookjuk.controller.rent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentController {
	
	@GetMapping("/rent")
	public String rent() {
		log.info("RentController rent()");
		return "/rent/rent";
	}
	
	
	
	
}
