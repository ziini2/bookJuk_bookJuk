package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
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
    public PaymentService() {
        this.iamportClient = new IamportClient("3508021822427821", "LULDSgki9IASha2mkulBaoL9bGKSiSREhh1H8bSnb3Hv2GTJoNGCdP0yRv7ogJCJnjvdj6SGIOFzu1K3");
    }

    public PaymentRes validatePayment(String impUid) throws IamportResponseException, IOException {
        // Iamport API를 통해 imp_uid로 결제 정보 조회
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();

        if (payment == null) {
            throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다.");
        }

        // 결제 검증 로직 추가 (결제 금액 및 상태 확인)
        int paidAmount = payment.getPayment_price().intValue();
        PaymentStatus status = payment.getPayment_status();

        if (status != PaymentStatus.SUCCESSFUL) {
            throw new IllegalArgumentException("결제 상태가 유효하지 않습니다.");
        }

        // 결제 성공 후 Payment 객체 생성
        Payment newPayment = Payment.builder()
            .user_num(null) // 여기서 user_num에 대한 값 설정 필요
            .payment_status(status)
            .payment_price((long) paidAmount)
            .payment_method(payment.getPayment_method()) // 결제 수단
            .merchant_mid(payment.getMerchant_mid()) // 주문 번호
            .req_date(LocalDateTime.now()) // 요청 일시
            .build();

        // 결제 정보를 데이터베이스에 저장
        savePayment(newPayment);
        
        // 결제 성공으로 PaymentRes 객체 반환
        return new PaymentRes(payment.getMerchant_mid(), payment.getPayment_id(), paidAmount, status);
    }
    
    public void savePayment(Payment payment) {
        paymentRepository.save(payment); // 데이터베이스에 저장
    }
}

