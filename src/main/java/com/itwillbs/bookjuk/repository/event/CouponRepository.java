package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
