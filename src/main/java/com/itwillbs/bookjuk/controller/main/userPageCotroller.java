package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Slf4j
@Controller
public class userPageCotroller {

	@GetMapping("/")
	public String userMain(Model model) {
		log.info("userMain");
		log.info("userRole: {}", SecurityUtil.getUserRoles());
		log.info("userName: {}", SecurityUtil.getUserName());
		log.info("userNum: {}", SecurityUtil.getUserNum());
		//회원의 이름을 가져오기!
		model.addAttribute("userName", SecurityUtil.getUserName());

		if (SecurityUtil.hasRole("ROLE_INACTIVE")){
			return "redirect:/login/phone"; // 권한이 "ROLE_INACTIVE"라면 리다이렉트
		}
		return "userMain";
	}
}
	

