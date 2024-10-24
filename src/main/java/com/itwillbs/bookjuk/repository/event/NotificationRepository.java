package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.Coupon;
import com.itwillbs.bookjuk.entity.event.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
