package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.util.SecurityUtil;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	 private final IamportClient iamportClient;
	 private final PaymentRepository paymentRepository;
	 private final UserContentRepository userContentRepository;
	 
	 // 결제 정보를 DB에서 조회하는 메서드
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();  // 모든 결제 정보를 반환
    }
    
//    //유저의 포인트 값을 가져오는 메서드
//    public Long getUserPoints(Long userNum) {
//    // userNum을 이용해서 UserEntity를 찾고, 그에 연관된 UserInfoEntity에서 포인트를 가져옴
//    UserContentEntity userEntity = userRepository.findByUserNum(userNum);
//    if (userEntity != null && userEntity.getUserInfo() != null) {
//        return userEntity.getUserInfo().getPoints(); // UserInfoEntity에서 포인트 값 반환
//    }
//    return 0L; // 포인트 값이 없으면 0 반환
//    
	@Autowired
    public PaymentService(@Value("${iamport.api_key}") String apiKey,
                          @Value("${iamport.api_secret}") String apiSecret,
                          PaymentRepository paymentRepository,
                          UserContentRepository userContentRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
        this.userContentRepository = userContentRepository;
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
    	        
    	        //로그인된 유저 번호
    	        Long user = SecurityUtil.getUserNum();
    	        
    	   
    	        System.out.println("로그인된 유저 번호: " + user);
    	        
    	        
    	        UserContentEntity userContentEntity = userContentRepository.findByUserNum(user);
    	        
    	        System.out.println("유저엔티티 " + userContentEntity.toString());
    	        
    	        
    	        // System.out.println(userEntity);
    	        //결제 정보 DB에 저장
    	         PaymentEntity paymentEntity = PaymentEntity.builder()
	            .paymentId(iamportPayment.getImpUid())
	            .merchantUid(iamportPayment.getMerchantUid())
	            .paymentPrice(iamportPayment.getAmount().longValue())
	            .userContentEntity(userContentEntity)
	            .paymentMethod(iamportPayment.getPayMethod())
	            .paymentStatus(paymentDTO.getStatus())
	            .priceName(paymentDTO.getPriceName())
	            .reqDate(LocalDateTime.now())
	            .build();
    	        
    	        paymentRepository.save(paymentEntity);

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
    
 // 결제 취소 메서드
    public void cancelPayment(String paymentId) throws IamportResponseException, IOException {
        try {
            //결제 정보 데이터베이스에서 조회
            PaymentEntity paymentEntity = paymentRepository.findByPaymentId(paymentId);
            if (paymentEntity == null) {
                throw new IllegalArgumentException("유효하지 않은 결제 ID입니다.");
            }

            // 결제 취소 데이터 생성
            CancelData cancelData = new CancelData(paymentId, true);

            // 아임포트 API를 통해 결제 취소 요청
            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            if (cancelResponse.getResponse() == null || !"cancelled".equals(cancelResponse.getResponse().getStatus())) {
                throw new IllegalArgumentException("결제 취소가 실패했습니다.");
            }

            // 취소가 성공했을 경우 DB 업데이트
            paymentEntity.setPaymentStatus(PaymentStatus.CANCEL);
            paymentEntity.setReqDate(LocalDateTime.now());
            paymentRepository.save(paymentEntity);

            System.out.println("결제 취소 성공");

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

