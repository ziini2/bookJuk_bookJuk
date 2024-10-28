package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.domain.login.UserRole;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class userPageCotroller {
	
	@GetMapping("/")
	public String userMain(HttpSession session) {
		log.info("userMain");

		Object roleObj = session.getAttribute("role");
		log.info("roleObj: {}", roleObj);

		if ("ROLE_INACTIVE".equals(roleObj)){
			return "redirect:/phone";
		}
		return "userMain";
	}
}
	

