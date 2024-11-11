package com.itwillbs.bookjuk.controller.member;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.domain.member.PageDTO;
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
	
	@GetMapping("/booklist")
	public String booklist(HttpServletRequest request) {
		log.info("MemberController booklist()");
		
//		String search = request.getParameter("search");
//		
//		PageDTO pageDTO = new PageDTO();
//		pageDTO.setSearch(search);
		
		return "/member/booklist";
	}
	
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
		log.info("MemberController usercontent()");
		
		String userId = SecurityContextHolder
				.getContext().getAuthentication().getName();
		
		
		Optional<UserEntity> userEntity = memberpageService.findByuserId(userId);
		
		model.addAttribute("userEntity", userEntity.get());
		
		return "/member/usercontent";
	}
	
	@GetMapping("/userctupdate")
	public String userctupdate() {
		log.info("MemberController userctupdate()");
		return "/member/userctupdate";
	}

}
