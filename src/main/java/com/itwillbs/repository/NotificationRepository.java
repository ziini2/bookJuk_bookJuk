package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Coupon;
import com.itwillbs.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
