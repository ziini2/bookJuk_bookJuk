package com.itwillbs.bookjuk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.Payment;


public interface PaymentRepository extends JpaRepository<Payment, String> {
	List<Payment> findAll();  //결제 정보 조회
}
