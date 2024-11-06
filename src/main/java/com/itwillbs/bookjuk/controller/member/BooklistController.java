package com.itwillbs.bookjuk.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.bookjuk.domain.member.PageDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class BooklistController {
	
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
	public String usercontent() {
		log.info("MemberController usercontent()");
		return "/member/usercontent";
	}
	
	@GetMapping("/userctupdate")
	public String userctupdate() {
		log.info("MemberController userctupdate()");
		return "/member/userctupdate";
	}

}
