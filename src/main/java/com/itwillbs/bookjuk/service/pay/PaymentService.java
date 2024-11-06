package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.entity.pay.Payment;
import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;


@Service
public class PaymentService {

    private final IamportClient iamportClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(@Value("${iamport.api_key}") String apiKey,
                          @Value("${iamport.api_secret}") String apiSecret) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    public PaymentDTO validatePayment(String impUid) throws IamportResponseException, IOException {
        // Iamport API를 통해 imp_uid로 결제 정보 조회
        IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.paymentByImpUid(impUid);
        com.siot.IamportRestClient.response.Payment iamportPayment = response.getResponse();

        if (iamportPayment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다.");
        }

        // 결제 검증 로직 추가 (결제 금액 및 상태 확인)
        int paidAmount = iamportPayment.getAmount().intValue();
        String statusString = iamportPayment.getStatus(); // 예: "paid" 또는 "failed"
        PaymentStatus status = PaymentStatus.valueOf(statusString.toUpperCase());
        if (status != PaymentStatus.SUCCESSFUL) {
            throw new IllegalArgumentException("결제 상태가 유효하지 않습니다.");
        }
    
        // 결제 성공 후 Payment 객체 생성
        Payment newPayment = Payment.builder()
//            .user_num(null) // 여기서 user_num에 대한 값 설정 필요
            .payment_status(status)
            .payment_price((long) paidAmount)
            .payment_method(iamportPayment.getPayMethod()) // 결제 수단
            .merchant_uid(iamportPayment.getMerchantUid()) // 주문 번호
            .req_date(LocalDateTime.now()) // 요청 일시
            .build();

        // 결제 정보를 데이터베이스에 저장
        savePayment(newPayment);
        
        // 결제 성공으로 PaymentRes 객체 반환
        return new PaymentDTO(iamportPayment.getMerchantUid(), iamportPayment.getCustomerUid(), paidAmount, status);

    }
    
    public void savePayment(Payment payment) {
        paymentRepository.save(payment); // 데이터베이스에 저장
    }
}

