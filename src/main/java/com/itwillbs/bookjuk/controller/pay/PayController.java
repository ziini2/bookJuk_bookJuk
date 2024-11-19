package com.itwillbs.bookjuk.controller.pay;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import com.itwillbs.bookjuk.service.pay.PaymentService;
import com.itwillbs.bookjuk.util.SecurityUtil;


@Controller
public class PayController {
  
 private final PaymentService paymentService;

    @Autowired
    public PayController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    //결제 정보 조회
    @GetMapping("/admin/pay_list")
    public String payList(Model model,
    		@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "15", required = false) int size,
			@RequestParam(value = "search", defaultValue = "", required = false) String search) {
    	

		Pageable pageable = PageRequest.of(page-1, size, Sort.by("paymentId").descending());

//		Page<StoreEntity> storeList = customerService.getStoreList(pageable);
		Page<PaymentEntity> paymentList = paymentService.findByPaymentIdContaining(pageable, search);
		
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		// 전체 페이지 개수
		model.addAttribute("totalPages", paymentList.getTotalPages());
		// 한화면에 보여줄 페이지 개수 설정
		int pageBlock = 15;
		int startPage = (page - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if(endPage > paymentList.getTotalPages()) {
			endPage = paymentList.getTotalPages();
		}
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
    	
    	
        //List<PaymentEntity> payments = paymentService.getAllPayments();
        model.addAttribute("paymentList", paymentList);


        return "pay/pay_list";

    }
	    
	@GetMapping("/admin/point_deal_list")
	public String refund() {

		return "/pay/point_deal_list";
	}
	 
//	@GetMapping("/cart")
//	public String cart() {
//
//		return "/pay/cart";
//	}
	
	
//	@GetMapping("/pay_add")
//	public String payAdd(Model model) {
//
//	Long userPoint = paymentService.getUserPoint(SecurityUtil.getUserNum()); // 서비스에서 포인트 조회
//	String userEmail = paymentService.getUserEmail(SecurityUtil.getUserNum());
//	List<PaymentEntity> userPayments = paymentService.getPaymentsByMemberNum(SecurityUtil.getUserNum());
//	
//	model.addAttribute("userNum", SecurityUtil.getUserNum());
//	model.addAttribute("userPoint", userPoint);
//	model.addAttribute("userEmail", userEmail);
//	model.addAttribute("userPayments", userPayments); 
//
//	return "/pay/pay_add";
//	}
	
//	@GetMapping("/pay_add")
//	public String payAdd(Model model) {
//	    Long userNum = SecurityUtil.getUserNum();  // 로그인된 유저의 userNum을 가져옴
//	    Long userPoint = paymentService.getUserPoint(userNum); // 서비스에서 포인트 조회
//	    String userEmail = paymentService.getUserEmail(userNum);
//	    List<PaymentEntity> userPayments = paymentService.getPaymentsByMemberNum(userNum);
//
//	    model.addAttribute("userNum", userNum);
//	    model.addAttribute("userPoint", userPoint);
//	    model.addAttribute("userEmail", userEmail);
//	    model.addAttribute("userPayments", userPayments); 
//
//	    return "/pay/pay_add";
//	}
	@GetMapping("/user/pay_add")
	public String payAdd(Model model) {
	    Long userNum = SecurityUtil.getUserNum();  // 로그인된 유저의 userNum을 가져옴
	    Optional<UserEntity> userEntity = paymentService.getUserNum(userNum);  // userNum을 통해 memberNum을 조회

	    // userEmail은 user 테이블에서 userNum으로 조회
	    String userEmail = paymentService.getUserEmail(userNum);

	    // userPoint는 user_content 테이블에서 memberNum으로 조회
	    int userPoint = paymentService.getUserPoint(userEntity.get().getUserNum());

	    // 결제 내역을 조회할 때 memberNum을 사용
	    List<PaymentEntity> userPayments = paymentService.getPaymentsByMemberNum(userNum);

	    // 모델에 추가
	    model.addAttribute("userNum", userNum);
	    model.addAttribute("userPoint", userPoint);
	    model.addAttribute("userEmail", userEmail);
	    model.addAttribute("userPayments", userPayments);

	    return "pay/pay_add";  // 뷰 이름
	}


	
	
	
}

