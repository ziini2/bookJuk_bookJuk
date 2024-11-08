package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
	PaymentEntity findByPaymentId(String paymentId);
	List<PaymentEntity> findAllByOrderByReqDateDesc(); //모든 결제 정보 조회 
	List<PaymentEntity> findByUserContentEntity_UserNumOrderByReqDateDesc(Long userNum); //사용자 본인 결제 정보만 조회
}