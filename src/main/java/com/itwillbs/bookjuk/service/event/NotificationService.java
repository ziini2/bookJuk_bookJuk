package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notiRepository;
	
	public List<NotificationEntity> getNoti(UserEntity recipient) {
		return notiRepository.findByRecipient(recipient);
	}
	
	public void sendNoti(NotificationEntity noti) {
		noti.setNotiSentDate(new Timestamp(System.currentTimeMillis()));
		notiRepository.save(noti);
	}

	
}
