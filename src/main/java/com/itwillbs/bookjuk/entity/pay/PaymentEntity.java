
package com.itwillbs.bookjuk.entity.pay;

import java.time.LocalDateTime;

import com.itwillbs.bookjuk.domain.pay.PaymentStatus;
import com.itwillbs.bookjuk.entity.UserContentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class PaymentEntity {
 
	//결제ID
	@Id
	private String paymentId;
	
	//회원번호(user_content 테이블 참조)
	@ManyToOne
	@JoinColumn(name = "member_num")
	private UserContentEntity userContentEntity;  // UserContent 테이블 참조
	
	//결제상태
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	//결제금액
	@Column(nullable = false)
	private int paymentPrice;
	
	//요청일시
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime reqDate;
	 
	//결제수단
	@Column(nullable = false)
	private String paymentMethod;
	
	//주문번호
	@Column(nullable = false)
	private String merchantUid;
	
	//결제품목
	@Column(nullable = false)
	private String priceName;
	
	//포인트 사용 여부
	@Column(nullable = false)
    private boolean pointUsed;
	
	// userPoint를 가져오는 메서드
    public int getUserPoint() {
    return this.userContentEntity.getUserPoint();  // UserContentEntity를 통해 userPoint 접근
    }
    
    public void setPaymentPrice(int paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }
    
}


