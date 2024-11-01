package com.itwillbs.bookjuk.repository.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.Notification;
import com.itwillbs.bookjuk.entity.UserEntity;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByRecipient(UserEntity recipient);
}