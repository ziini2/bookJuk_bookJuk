package com.itwillbs.bookjuk.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.event.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, NotificationRepositoryCustom {

}
