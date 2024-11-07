package com.itwillbs.bookjuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.Payment;


public interface PaymentRepository extends JpaRepository<Payment, String> {
  
}
