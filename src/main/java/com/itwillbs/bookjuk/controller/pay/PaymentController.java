package com.itwillbs.bookjuk.controller.pay;

import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.service.pay.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpRequest;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 결제 처리 및 검증 엔드포인트
    @PostMapping("/process")
    public ResponseEntity<?> validatePayment(@RequestParam PaymentDTO paymentDTO) {
    	System.out.println("/payment/process--------------------");
    	//@RequestBody PaymentDTO paymentDTO

    	System.out.println(paymentDTO);
        try {
        	// 결제 정보 검증 및 저장
//            paymentService.verifyAndSavePayment(paymentDTO);
            return ResponseEntity.ok("결제 검증 및 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 검증에 실패하였습니다: " + e.getMessage());
        }
    }
}
