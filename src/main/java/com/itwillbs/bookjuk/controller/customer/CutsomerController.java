package com.itwillbs.bookjuk.controller.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class CutsomerController {

	private final CustomerService customerService;

	@GetMapping("/admin/store/store_list")
	public String storeList(Model model,
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "15", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("storeCode").descending());

//		Page<StoreEntity> storeList = customerService.getStoreList(pageable);
		Page<StoreEntity> storeList = customerService.findByStoreNameContaining(pageable, search);
		
		model.addAttribute("storeList", storeList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		// 전체 페이지 개수
		model.addAttribute("totalPages", storeList.getTotalPages());
		// 한화면에 보여줄 페이지 개수 설정
		int pageBlock = 15;
		int startPage = (page - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if(endPage > storeList.getTotalPages()) {
			endPage = storeList.getTotalPages();
		}
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "customer/store";
	}
	
	@PostMapping("/admin/store/addStore")
	public String addStore(StoreEntity storeEntity) {
		customerService.addStore(storeEntity);
		log.info(storeEntity.toString());
		return "redirect:/admin/store/store_list";
	}

	@GetMapping("/admin/store/store_info")
	public String storeInfo(@RequestParam(value = "storeCode") Long storeCode) {
		log.info(storeCode.toString());
		return "customer/store_info";
	}

	@GetMapping("/admin/user/user_list")
	public String userList() {
		return "customer/user";
	}

	@GetMapping("/admin/user/user_info")
	public String userInfo() {
		return "customer/user_info";
	}
}
