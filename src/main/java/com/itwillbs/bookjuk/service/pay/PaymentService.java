package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.entity.pay.Payment;
import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

	 private final IamportClient iamportClient;
	 private final PaymentRepository paymentRepository;

	@Autowired
    public PaymentService(@Value("${iamport.api_key}") String apiKey,
                          @Value("${iamport.api_secret}") String apiSecret,
                          PaymentRepository paymentRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
    }

	 // 결제 검증 후 저장
    public void verifyAndSavePayment(PaymentDTO paymentDTO) throws IamportResponseException, IOException {
        // 아임포트 서버에 imp_uid로 결제 정보 요청
        IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.paymentByImpUid(paymentDTO.getPaymentId());
        com.siot.IamportRestClient.response.Payment iamportPayment = response.getResponse();
        System.out.println(iamportClient.paymentByImpUid(paymentDTO.getPaymentId()));
        System.out.println(iamportPayment);
        // 결제 검증 실패 시 예외 처리
        if (iamportPayment == null) {
            throw new IllegalArgumentException("유효하지 않은 결제입니다.");
            
        }
        System.out.println("verifyAndSavePayment1");

//        // 검증 조건 예: 금액이 일치하는지, 결제 상태가 "paid"인지 확인
        if (!iamportPayment.getStatus().equals("paid") || iamportPayment.getAmount().longValue() != paymentDTO.getPaidAmount()) {
            throw new IllegalArgumentException("결제 정보가 유효하지 않습니다.");
        }
        System.out.println("verifyAndSavePayment2");
        // 결제 정보가 유효한 경우, 데이터베이스에 저장
        Payment payment = Payment.builder()
            .payment_id(iamportPayment.getImpUid())
            .merchant_uid(iamportPayment.getMerchantUid())
            .payment_price(iamportPayment.getAmount().longValue())
            .user_num(paymentDTO.getUserNum())
            .payment_method(iamportPayment.getPayMethod()) // 결제 수단
            .payment_status(paymentDTO.getStatus()) // 결제 상태
        	.price_name(paymentDTO.getPriceName())
            .req_date(LocalDateTime.now()) // 요청 일시
            .build();
        
      //Payment payment = Payment.builder()
      //.payment_id(paymentDTO.getPaymentId())
      //.merchant_uid(paymentDTO.getMerchantMid())
      //.payment_price(paymentDTO.getPaidAmount().longValue())
      //.user_num(paymentDTO.getUserNum())
      //.payment_method(paymentDTO.getPaymentMethod()) // 결제 수단
      //.payment_status(PaymentStatus.SUCCESSFUL) // 결제 상태
      //.price_name(paymentDTO.getPriceName())
      //.req_date(LocalDateTime.now()) // 요청 일시
      //.build();
        System.out.println("Payment to be saved: " + payment); // 로그 추가
        paymentRepository.save(payment); // 결제 정보를 데이터베이스에 저장
    }

}

