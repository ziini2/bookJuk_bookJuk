package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notiRepository;
	
	public List<NotificationEntity> getNoti(UserEntity recipient) {
		return notiRepository.findByNotiRecipient(recipient);
	}
	
	public void sendNoti(NotificationEntity noti) {
		noti.setNotiSentDate(new Timestamp(System.currentTimeMillis()));
		notiRepository.save(noti);
	}

	
}
