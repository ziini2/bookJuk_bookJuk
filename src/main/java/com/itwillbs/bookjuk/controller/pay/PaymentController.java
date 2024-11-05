package com.itwillbs.bookjuk.controller.pay;

import com.itwillbs.bookjuk.service.pay.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 결제 검증 엔드포인트
    @GetMapping("/validate/{impUid}")
    public ResponseEntity<?> validatePayment(@PathVariable String impUid) {
        try {
            // 결제 검증 서비스 호출
            PaymentRes paymentRes = paymentService.validatePayment(impUid);
            return ResponseEntity.ok(paymentRes); // 성공 시 결제 정보 반환
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.badRequest().body("결제 검증에 실패하였습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("오류: " + e.getMessage());
        }
    }
}
