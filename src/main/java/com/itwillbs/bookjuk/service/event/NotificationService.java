package com.itwillbs.bookjuk.service.event;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.dto.NotiDTO;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notiRepository;
	
	public Page<NotiDTO> getAllEvent(Long userNum, Pageable pageable) {
		try {
			// 알림 관리 페이지
			if(userNum == 0L) {
				return notiRepository.findAll(pageable).map(this::convertToDto);
			}else {
			// 유저 알림 페이지
				return notiRepository.findByNotiRecipient_UserNum(userNum, pageable).map(this::convertToDto);
			}
	    } catch (Exception e) {
	        log.error("Error fetching all notis: ", e);
	        return Page.empty(pageable);
	    }
	}
	
	private NotiDTO convertToDto(NotificationEntity notificationEntity) {
        return NotiDTO.builder()
        		.notiId(notificationEntity.getNotiId())
        		.notiRecipient(notificationEntity.getNotiRecipient().getUserNum())
        		.notiSender(notificationEntity.getNotiSender().getUserNum())
        		.notiContent(notificationEntity.getNotiContent())
        		.notiType(notificationEntity.getNotiType())
        		.notiStatus(notificationEntity.getNotiStatus())
        		.notiCreationDate(notificationEntity.getNotiCreationDate())
        		.notiSentDate(notificationEntity.getNotiSentDate())
        		.recipient(notificationEntity.getNotiRecipient().getUserId())
        		.sender(notificationEntity.getNotiSender().getUserName())
        		.build();
    }

	public Page<NotiDTO> getFilteredEvent(Long userNum, String searchCriteria, String searchKeyword, List<Map<String, String>> filter,
			Pageable pageable) {
		try {
	        return notiRepository.notiTable(userNum, searchCriteria, searchKeyword, 
	        		filter, pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        log.error("Error fetching filtered events: ", e);
	        return Page.empty(pageable); // 예외 발생 시 빈 페이지 반환
	    }
	}	

	public NotiDTO getNotiDetail(Long notiId) {
		NotificationEntity notificationEntity = notiRepository.findById(notiId).orElse(null);
		return NotiDTO.builder()
        		.notiId(notificationEntity.getNotiId())
        		.notiRecipient(notificationEntity.getNotiRecipient().getUserNum())
        		.notiSender(notificationEntity.getNotiSender().getUserNum())
        		.notiContent(notificationEntity.getNotiContent())
        		.notiType(notificationEntity.getNotiType())
        		.notiStatus(notificationEntity.getNotiStatus())
        		.notiCreationDate(notificationEntity.getNotiCreationDate())
        		.notiSentDate(notificationEntity.getNotiSentDate())
        		.recipient(notificationEntity.getNotiRecipient().getUserId())
        		.sender(notificationEntity.getNotiSender().getUserName())
        		.build();
	}

	public boolean getNotiByIdAndUserNum(Long notiId, Long userNum) {
		return notiRepository.existsByNotiIdAndNotiRecipient_UserNum(notiId, userNum);
	}

	
}
