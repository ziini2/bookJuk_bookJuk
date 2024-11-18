package com.itwillbs.bookjuk.controller.customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CutsomerController {

	private final CustomerService customerService;

	@GetMapping("/admin/store/store_list")
	public String storeList(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "15", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("storeRegiDate").descending());

//		Page<StoreEntity> storeList = customerService.getStoreList(pageable);
		// 검색어받아서 페이지네이션
		Page<StoreEntity> storeList = customerService.findByStoreNameContaining(pageable, search);
		
		// 뷰에 필요한것 저장
		model.addAttribute("storeList", storeList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		// 전체 페이지 개수
		model.addAttribute("totalPages", storeList.getTotalPages());
//		log.info("storeList 개수 {}", storeList.getSize());
		// 한화면에 보여줄 페이지 개수 설정
		int pageBlock = 15;
		int startPage = (page - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if (endPage > storeList.getTotalPages()) {
			endPage = storeList.getTotalPages();
		}
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "customer/store";
	}

	// 지점 등록 함수
	@PostMapping("/admin/store/addStore")
	public String addStore(StoreEntity storeEntity) {
		storeEntity.setStoreStatus("open");
		customerService.addStore(storeEntity);
		log.info(storeEntity.toString());
		return "redirect:/admin/store/store_list";
	}

	// 한 지점정보를 가져오는 함수
	@GetMapping("/admin/store/store_info")
	public String storeInfo(@RequestParam(value = "storeCode") Long storeCode, Model model) {
		log.info(storeCode.toString());
		
		Optional<StoreEntity> storeInfo = customerService.findById(storeCode);

		model.addAttribute("storeInfo", storeInfo.get());

		return "customer/store_info";
	}

	// 지점 정보 수정 함수
	@PostMapping("/admin/store/store_update")
	public String storeUpdate(StoreEntity storeEntity) {
		log.info("store_update" + storeEntity.getStoreCode());

		customerService.storeUpdate(storeEntity);

		return "redirect:/admin/store/store_info?storeCode=" + storeEntity.getStoreCode();
	}
	
	// 지점 폐점 처리하는 함수(비동기)
	@ResponseBody
	@PostMapping("/admin/store/store_delete") // json, Map으로 받을수 있음 
	public String storeDelete(@RequestBody Map<String, Object> requestBody) {
		log.info("storeDelete : " + requestBody.get("storeCode"));

		customerService.deleteStore(Long.valueOf(requestBody.get("storeCode").toString()), 
									requestBody.get("status").toString());

		return requestBody.get("status").toString();
	}

	// 유저목록 가져오는 함수
	@GetMapping("/admin/user/user_list")
	public String userList(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "15", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {

		Pageable pageable = PageRequest.of(page - 1, size
//				,Sort.by("storeCode").descending()
		);

		Page<UserEntity> userList = customerService.findByUserContaining(pageable, search);

		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		// 전체 페이지 개수
		model.addAttribute("totalPages", userList.getTotalPages());
		// 한화면에 보여줄 페이지 개수 설정
		int pageBlock = 15;
		int startPage = (page - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if (endPage > userList.getTotalPages()) {
			endPage = userList.getTotalPages();
		}
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		model.addAttribute("userList", userList);

		return "customer/user";
	}

	// 유저 정보 가져온는 함수
	@GetMapping("/admin/user/user_info")
	public String userInfo(@RequestParam(value = "user") Long userNum, Model model) {
//		log.info(userNum + "");

		model.addAttribute("userInfo", customerService.getUserInfo(userNum));

		return "customer/user_info";
	}

	// 유저 탈퇴상태 함수 (비동기)
	@ResponseBody
	@PostMapping("/admin/user/userDelete")
	public String userDelete(@RequestBody Map<String, Object> requestBody) {
		log.info("userdel {}", requestBody.get("userNum"));
		customerService.deleteUser(Long.valueOf(requestBody.get("userNum").toString()),
								   Boolean.valueOf(requestBody.get("status").toString()));
		return requestBody.get("status").toString();
	}
}
