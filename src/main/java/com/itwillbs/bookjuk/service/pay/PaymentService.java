package com.itwillbs.bookjuk.service.pay;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.dto.PaymentDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
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
@Transactional
public class PaymentService {

	 private final IamportClient iamportClient;
	 private final PaymentRepository paymentRepository;
	 private final UserContentRepository userContentRepository;
	 private final UserRepository userRepository;
	 


	//모든 결제 정보를 DB에서 조회하는 메서드
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAllByOrderByReqDateDesc();  //모든 결제 정보를 최신순으로 반환
    }
    
    // 검색어를 기준으로 결제 목록 조회 (검색 기능)
    public List<PaymentEntity> searchPayments(String searchValue) {
        
    	return paymentRepository.findAllByOrderByReqDateDesc();
    }
    
    public List<PaymentEntity> getPaymentsByMemberNum(Long memberNum) {
    //로그인한 유저의 결제 내역만 가져오기(유저번호 기준으로)
    return paymentRepository.findByUserContentEntity_MemberNumOrderByReqDateDesc(memberNum);
    }
    
	@Autowired
    public PaymentService(@Value("${iamport.api_key}") String apiKey,
                          @Value("${iamport.api_secret}") String apiSecret,
                          PaymentRepository paymentRepository,
                          UserContentRepository userContentRepository,
                          UserRepository userRepository) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.paymentRepository = paymentRepository;
        this.userContentRepository = userContentRepository;
        this.userRepository = userRepository;
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
	        
	        
	        UserContentEntity userContentEntity = userContentRepository.findByMemberNum(user);
	        
	        System.out.println("유저엔티티 " + userContentEntity.toString());
	        
	        boolean pointUsed = paymentDTO.getPaidAmount() != iamportPayment.getAmount().intValue();

	        //결제 정보 DB에 저장
	         PaymentEntity paymentEntity = PaymentEntity.builder()
            .paymentId(iamportPayment.getImpUid())
            .merchantUid(iamportPayment.getMerchantUid())
            .paymentPrice(iamportPayment.getAmount().intValue())
            .userContentEntity(userContentEntity)
            .paymentMethod(iamportPayment.getPayMethod())
            .paymentStatus(paymentDTO.getStatus())
            .priceName(paymentDTO.getPriceName())
            .reqDate(LocalDateTime.now())
            .pointUsed(pointUsed)
            .build();
	        
	        paymentRepository.save(paymentEntity);
	        // 결제 금액에 따른 포인트 업데이트
	        int amount = iamportPayment.getAmount().intValue(); //결제 금액 정수형으로 변환
	        updateUserPoint(user, amount); // 결제 금액을 기반으로 포인트 업데이트
	        

	    } catch (IamportResponseException e) {
	        // 아임포트 API 호출 실패 시
	        System.out.println("아임포트 API 호출 실패: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("아임포트 API 호출 실패", e);  // 예외를 던져서 상위 로직에서 처리할 수 있도록 합니다.

	    } catch (IOException e) {
	        // 네트워크 오류
	        System.out.println("네트워크 오류: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("네트워크 오류", e);  // 예외를 던져서 상위 로직에서 처리할 수 있도록 합니다.

	    } catch (Exception e) {
	        // 기타 예기치 않은 오류 처리
	        System.out.println("예기치 못한 오류: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("예기치 못한 오류 발생", e);  // 예외를 던져서 상위 로직에서 처리할 수 있도록 합니다.
	    }
	}
    
	 
	 public Long getUserNumByMemberNum(Long memberNum) {
	        return userContentRepository.findUserNumByMemberNum(memberNum);
	    }
    
    //유저 이메일 가져오기(결제 정보)
    public String getUserEmail(Long userNum) {
    	 UserEntity userEntity = userRepository.findByUserNum(userNum); 
    	 return userEntity.getUserEmail();
    }
    
    
    
    
    //유저의 포인트 값 가져오기
    public Long getUserPoint(Long userEntity) {
    	Optional<UserContentEntity> nowPoint = userContentRepository.findById(userEntity); // memberNum으로 엔티티 조회
    	return nowPoint.map(content -> (long) content.getUserPoint()) // int -> long으로 변환
                .orElse(0L); // Optional이 비어있으면 기본값 0L을 반환
    }
    
    //결제가 완료되었을 때 포인트로 업데이트
    public void updateUserPoint(Long memberNum, int amount) {
    UserContentEntity updatePoint = userContentRepository.findByMemberNum(memberNum);

	 int nowPoint = updatePoint.getUserPoint(); // 현재 포인트 가져오기
	 
	 //결제 금액에 따라 추가할 포인트 계산
     int pointsToAdd = calculatePoints(amount); // 금액에 따른 포인트 계산 메서드 호출
     updatePoint.setUserPoint(nowPoint + pointsToAdd); // 계산된 포인트를 추가
     
     userContentRepository.save(updatePoint); // 업데이트된 포인트 저장
    	
    }    
    
    private int calculatePoints(int amount) {
        //포인트 조정
    	if (amount == 100) {
            return 5000;
        } else if (amount == 200) {
            return 10000;
        } else if (amount == 300) {
            return 30000;
        } else if (amount == 400) {
            return 50000;
        } else if (amount == 500) {
            return 100000;
        }
    	
        return amount;
    }
    
    //결제 취소
    public void cancelPayment(String paymentId) throws IamportResponseException, IOException {
        try {
            
        	PaymentEntity paymentEntity = paymentRepository.findByPaymentId(paymentId);
        	

            if (paymentEntity == null) {
                throw new IllegalArgumentException("결제 정보가 없습니다.");
            }

            //포인트 사용 이력 있을 경우 취소 불가
            if (paymentEntity.isPointUsed()) {
                System.out.println("이 결제는 포인트가 사용되었으므로 취소할 수 없습니다.");
                throw new IllegalArgumentException("포인트가 사용된 결제는 취소할 수 없습니다.");
            }
            
            //결제 취소 요청 처리
            CancelData cancelData = new CancelData(paymentId, true);
            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            // 응답 로그 출력
            if (cancelResponse.getResponse() == null) {
                System.out.println("결제 취소 실패 - 응답 없음");
                System.out.println("에러 메시지: " + cancelResponse.getMessage());  // 오류 메시지 출력
            } else {
                System.out.println("결제 취소 응답 상태: " + cancelResponse.getResponse().getStatus());  // 예: cancelled
                System.out.println("결제 취소된 금액: " + cancelResponse.getResponse().getAmount());  // 취소된 금액
            }

         // 결제 취소 성공 시 DB 업데이트
            if (cancelResponse.getResponse() != null && "cancelled".equals(cancelResponse.getResponse().getStatus())) {
                
                // 결제 취소 성공 시 DB 업데이트
                paymentEntity.setPaymentStatus(PaymentStatus.CANCEL);
                paymentEntity.setReqDate(LocalDateTime.now());
                
                // 결제 금액을 음수로 설정 (취소된 금액)
                int amount = cancelResponse.getResponse().getAmount().intValue(); // 취소된 금액
                paymentEntity.setPaymentPrice(-amount);  // 결제 금액을 음수로 설정
                
                paymentRepository.save(paymentEntity);
                System.out.println("결제 취소 성공");
                
                // 결제 금액에 맞춰 포인트 차감 (음수가 아닌 원래 금액 사용)
                payCancelPoint(paymentId, Math.abs(amount));  // 결제 금액을 전달하여 포인트 차감 (음수 값은 제외)
            
            } else {
                throw new IllegalArgumentException("결제 취소가 실패했습니다.");
            }
        } catch (IamportResponseException e) {
            // 아임포트 API 호출 실패 시
            System.out.println("아임포트 API 호출 실패 (HTTP 코드: " + e.getHttpStatusCode() + "): " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // 네트워크 오류
            System.out.println("네트워크 오류: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // 예기치 않은 오류
            System.out.println("예기치 못한 오류 (" + e.getClass().getName() + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
 // 결제 취소 후 포인트 차감 메서드
    public void payCancelPoint(String paymentId, int amount) {
        // 결제 ID를 통해 결제 정보를 가져옵니다.
        PaymentEntity paymentEntity = paymentRepository.findByPaymentId(paymentId);

        if (paymentEntity != null) {
            Long user = SecurityUtil.getUserNum();
            UserContentEntity updateCancelPoint = userContentRepository.findByMemberNum(user);

            if (updateCancelPoint != null) {
                int currentPoints = updateCancelPoint.getUserPoint();  // 현재 포인트 가져오기
                
                // 결제 취소된 금액에 해당하는 포인트 차감
                int pointsToCancel = calculatePoints(amount);  // amount에 맞는 포인트 계산
                int updatedPoints = currentPoints - pointsToCancel;


                // 업데이트된 포인트 저장
                updateCancelPoint.setUserPoint(updatedPoints);
                userContentRepository.save(updateCancelPoint);
                System.out.println("포인트 차감 완료: " + pointsToCancel + "포인트");
            } else {
                System.out.println("사용자 정보가 없습니다.");
            }
        } else {
            System.out.println("결제 정보가 없습니다.");
        }
    }

	public Optional<UserEntity> getUserNum(Long userNum) {
		return userRepository.findById(userNum);
	}
}