package com.itwillbs.bookjuk.controller.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import com.itwillbs.bookjuk.service.pay.PaymentService;
import com.itwillbs.bookjuk.util.SecurityUtil;

import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import com.itwillbs.bookjuk.service.pay.PaymentService;

@Controller
public class PayController {
  
 private final PaymentService paymentService;

    @Autowired
    public PayController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    //결제 정보 조회
    @GetMapping("/admin/pay_list")
    public String payList(Model model) {
        List<PaymentEntity> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);


        return "pay/pay_list";

    }
	    
	@GetMapping("/admin/point_deal_list")
	public String refund() {

		return "/pay/point_deal_list";
	}
	 
	@GetMapping("/cart")
	public String cart() {

		return "/pay/cart";
	}
	
	@GetMapping("/pay_detail")
	public String payDetail() {

		return "/pay/pay_detail";
	}
	
	@GetMapping("/pay_add")
	public String payAdd(Model model) {

	Long userPoint = paymentService.getUserPoint(SecurityUtil.getUserNum()); // 서비스에서 포인트 조회
	String userEmail = paymentService.getUserEmail(SecurityUtil.getUserNum());
	List<PaymentEntity> userPayments = paymentService.getPaymentsByUserNum(SecurityUtil.getUserNum());
	
	model.addAttribute("userNum", SecurityUtil.getUserNum());
	model.addAttribute("userPoint", userPoint);
	model.addAttribute("userEmail", userEmail);
	model.addAttribute("userPayments", userPayments); 

	return "/pay/pay_add";
	}
	
}

