package com.itwillbs.bookjuk.controller.member;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.service.member.MemberpageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MemberpageController {
	
	private final MemberpageService memberpageService;
	
	@GetMapping("/bookcontent")
	public String bookcontent() {
		log.info("MemberController bookcontent()");
		return "/member/bookcontent";
	}
	
	@GetMapping("/userblist")
	public String userblist() {
		log.info("MemberController userblist()");
		return "/member/userblist";
	}
	
	@GetMapping("/usercontent")
	public String usercontent(HttpSession session, Model model) {
		log.info("MemberpageController usercontent()");
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info(userId);
				
		UserEntity userEntity = memberpageService.findByUserId(userId);
		model.addAttribute("userEntity", userEntity);
		log.info(userEntity.toString());
		
//		Optional<UserEntity> userEntity  = memberpageService.findById(userNum);
		
//		model.addAttribute("userEntity", userEntity.get());
		
		return "/member/usercontent";
	}
	
	@GetMapping("/userctupdate")
	public String userctupdate() {
		log.info("MemberController userctupdate()");
		return "/member/userctupdate";
	}

}
