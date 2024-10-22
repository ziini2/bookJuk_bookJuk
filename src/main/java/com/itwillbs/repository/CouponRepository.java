package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
