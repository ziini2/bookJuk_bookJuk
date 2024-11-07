package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
	List<PaymentEntity> findAll();  //결제 정보 조회
}
