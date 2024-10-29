package com.itwillbs.bookjuk.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userPageCotroller {
	
	@GetMapping("/")
		public String userMain() {
			
			return "userMain";
		}
	}
	

