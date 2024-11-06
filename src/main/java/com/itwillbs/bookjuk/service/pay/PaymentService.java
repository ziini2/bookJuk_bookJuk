package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.entity.pay.Payment;
import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

	 private final IamportClient iamportClient;
	 private final PaymentRepository paymentRepository;
	 
	 // 결제 정보를 DB에서 조회하는 메서드
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();  // 모든 결제 정보를 반환
    }
	    
	@Autowired
    public PaymentService(@Value("${iamport.api_key}") String apiKey,
                          @Value("${iamport.api_secret}") String apiSecret,
                          PaymentRepository paymentRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
    }

	//결제 검증 후 저장(클라이언트가 조작 가격 조작 못하게)
    public void verifyAndSavePayment(PaymentDTO paymentDTO) throws IamportResponseException, IOException {
    
    	  try {
    	        //Payment ID 로그 출력
    	        System.out.println("Payment ID: " + paymentDTO.getPaymentId()); // Payment ID 로그
    	        System.out.println("IamportClient instance: " + iamportClient); // IamportClient 인스턴스 상태 확인
    	        
    	        //아임포트 서버에 imp_uid로 결제 정보 요청
    	        IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.paymentByImpUid(paymentDTO.getPaymentId());
    	        System.out.println("Iamport response: " + response); // 응답 로그 출력
    	        
    	        //응답 확인
    	        if (response == null || response.getResponse() == null) {
    	            System.out.println("아임포트 응답 없음 - 유효하지 않은 결제 ID");
    	            throw new IllegalArgumentException("유효하지 않은 결제입니다.");
    	        }

    	        //결제 정보 확인 및 검증
    	        com.siot.IamportRestClient.response.Payment iamportPayment = response.getResponse();
    	        System.out.println("아임포트 결제 상태: " + iamportPayment.getStatus()); // 결제 상태 로그
    	        System.out.println("아임포트 결제 금액: " + iamportPayment.getAmount()); // 결제 금액 로그

    	        //검증 조건
    	        if (!"paid".equals(iamportPayment.getStatus()) || iamportPayment.getAmount().longValue() != paymentDTO.getPaidAmount()) {
    	            throw new IllegalArgumentException("결제 정보가 유효하지 않습니다.");
    	        }
    	        System.out.println("결제 검증 성공");

    	        //결제 정보 DB에 저장
    	        Payment payment = Payment.builder()
	            .payment_id(iamportPayment.getImpUid())
	            .merchant_uid(iamportPayment.getMerchantUid())
	            .payment_price(iamportPayment.getAmount().longValue())
	            .user_num(paymentDTO.getUserNum())
	            .user_name(paymentDTO.getUserName())
	            .payment_method(iamportPayment.getPayMethod())
	            .payment_status(paymentDTO.getStatus())
	            .price_name(paymentDTO.getPriceName())
	            .req_date(LocalDateTime.now())
	            .build();
    	        
    	        System.out.println("Payment to be saved: " + payment);
    	        paymentRepository.save(payment);

    	    } catch (IamportResponseException e) {
    	        System.out.println("아임포트 API 호출 실패: " + e.getMessage());
    	        e.printStackTrace();
    	    } catch (IOException e) {
    	        System.out.println("네트워크 오류: " + e.getMessage());
    	        e.printStackTrace();
    	    } catch (Exception e) {
    	        System.out.println("예기치 못한 오류: " + e.getMessage());
    	        e.printStackTrace();
    	    }
    
    }

}

