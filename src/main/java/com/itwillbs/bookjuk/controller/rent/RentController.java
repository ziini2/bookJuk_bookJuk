package com.itwillbs.bookjuk.controller.rent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.bookjuk.entity.RentEntity;
import com.itwillbs.bookjuk.service.rent.RentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentController {
	
	private final RentService rentService;
	
//	@GetMapping("/rent")
//	public String rent(HttpSession session, Model model) {
//		log.info("RentController rent()");
//		
//		return "/rent/rent";
//	}
	
	@GetMapping("/rent")
	public String rent(Model model,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "3", required = false) int size) {
		log.info("BoardController rent()");
//		import org.springframework.data.domain.Page;
//		import org.springframework.data.domain.PageRequest;
//		import org.springframework.data.domain.Pageable;
//		import org.springframework.data.domain.Sort;
		// 페이지번호 page
		// 한화면에 보여줄 글 개수 size
		// PageRequest 에서는 page 0부터 시작 => page-1 설정
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("num").descending());
		
		Page<RentEntity> rentList = rentService.getRentList(pageable);
		
		model.addAttribute("rentList",rentList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		//전체 페이지 개수
		model.addAttribute("totalPages", rentList.getTotalPages());
		
		//한화면에 보여줄 페이지 개수 설정
		int pageBlock = 3;
		int startPage = (page-1)/pageBlock*pageBlock+1;
		int endPage=startPage + pageBlock - 1;
		if(endPage > rentList.getTotalPages()) {
			endPage = rentList.getTotalPages();
		}
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "/rent/rent";
	}
	
	
}
