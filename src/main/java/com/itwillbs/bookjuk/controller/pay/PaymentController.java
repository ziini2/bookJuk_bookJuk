package com.itwillbs.bookjuk.controller.pay;

import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.service.pay.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //결제 처리 및 검증 엔드포인트
    @PostMapping("/process")
    public ResponseEntity<?> validatePayment(@RequestBody PaymentDTO paymentDTO) {
    	System.out.println("/payment/process--------------------");
    	System.out.println(paymentDTO);
        try {
        	// 결제 정보 검증 및 저장
            paymentService.verifyAndSavePayment(paymentDTO);
            return ResponseEntity.ok("결제 검증 및 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 검증에 실패하였습니다: " + e.getMessage());
        }
    }
    
    //결제 취소 엔드포인트
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(@RequestParam("paymentId") String paymentId) {
        try {
            // 결제 취소 메서드 호출
            paymentService.cancelPayment(paymentId);
            return ResponseEntity.ok("결제가 취소되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
//    
//	 // 결제가 완료되었을 때 호출되는 메서드
//    @PostMapping("/payment/complete")
//    public String paymentComplete(@RequestParam Long userNum, @RequestParam int amount) {
//    	System.out.println("/payment/complete--------------------");
//        // 결제 금액에 따라 포인트 업데이트
//        paymentService.updateUserPoint(userNum, amount);
//
//        return "결제가 완료되었습니다. 포인트가 업데이트되었습니다.";
//    }
}
