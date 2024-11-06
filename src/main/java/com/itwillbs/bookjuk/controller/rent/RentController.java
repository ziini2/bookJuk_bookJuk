package com.itwillbs.bookjuk.controller.rent;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.bookjuk.entity.RentEntity;
import com.itwillbs.bookjuk.service.rent.RentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentController {
	
	private final RentService rentService;
	
	@GetMapping("/admin/rent")
	public String rent(Model model,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size) {
		log.info("RentController rent()");
		
		// 페이지 로드 시 연체 상태 업데이트
        rentService.updateOverdueRentals();
		
		// 페이지번호 page
		// 한화면에 보여줄 글 개수 size
		// PageRequest 에서는 page 0부터 시작 => page-1 설정
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("rentNum").descending());
		
		Page<RentEntity> rentList = rentService.getRentList(pageable);
		
		model.addAttribute("rentList",rentList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		//전체 페이지 개수
		model.addAttribute("totalPages", rentList.getTotalPages());
		
		//한화면에 보여줄 페이지 개수 설정
		int pageBlock = 10;
		int startPage = (page-1)/pageBlock*pageBlock+1;
		int endPage=startPage + pageBlock - 1;
		if(endPage > rentList.getTotalPages()) {
			endPage = rentList.getTotalPages();
		}
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "/rent/rent";
	}
	
	@PostMapping("/updateReturnInfo")
	public ResponseEntity<?> updateReturnInfo(@RequestBody Map<String, Object> requestData) {
	    Number rentNum = (Number) requestData.get("rentNum"); // rentNum을 Number로 받아서 형변환
	    String returnInfo = (String) requestData.get("returnInfo");

	    try {
	        // rentNum을 Long이나 Integer로 변환하여 사용
	        rentService.updateReturnInfo(rentNum.longValue(), returnInfo);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        log.error("Failed to update return info", e);
	        return ResponseEntity.status(500).body("Failed to update return info");
	    }
	}
	
//	@GetMapping("/rent/search")
//    public ResponseEntity<List<RentEntity>> searchRent(
//        @RequestParam("criteria") String criteria,
//        @RequestParam("keyword") String keyword) {
//        
//        List<RentEntity> rentList = rentService.searchByCriteria(criteria, keyword);
//        return ResponseEntity.ok(rentList); // JSON 형식으로 데이터 반환
//    }
	
	@GetMapping("/rent/search")
	public ResponseEntity<Page<RentEntity>> searchRent(
	    @RequestParam("criteria") String criteria,
	    @RequestParam("keyword") String keyword,
	    @RequestParam(value = "page", defaultValue = "1") int page,
	    @RequestParam(value = "size", defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("rentNum").descending());
	    Page<RentEntity> rentList = rentService.searchByCriteria(criteria, keyword, pageable);
	    return ResponseEntity.ok(rentList); // JSON 형식으로 페이지 데이터 반환
	}

	
	@GetMapping("/admin/membersearch")
	public String membersearch() {
		log.info("RentController membersearch()");
		
		return "/rent/membersearch";
	}
	
	@GetMapping("/admin/booksearch")
	public String booksearch() {
		log.info("RentController booksearch()");
		
		return "/rent/booksearch";
	}
	
	
}
