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
	
	public Page<NotiDTO> getAllEvent(Pageable pageable) {
		try {
	        return notiRepository.findAll(pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
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
        		.transferStatus(notificationEntity.getNotiStatus())
        		.notiCreationDate(notificationEntity.getNotiCreationDate())
        		.notiSentDate(notificationEntity.getNotiSentDate())
        		.recipient(notificationEntity.getNotiRecipient().getUserId())
        		.sender(notificationEntity.getNotiSender().getUserName())
        		.build();
    }

	public Page<NotiDTO> getFilteredEvent(String searchCriteria, String searchKeyword, List<Map<String, String>> filter,
			Pageable pageable) {
		try {
	        return notiRepository.notiTable(searchCriteria, searchKeyword, 
	        		filter, pageable).map(this::convertToDto);
	    } catch (Exception e) {
	        // 로그 출력 및 예외 처리
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
        		.transferStatus(notificationEntity.getNotiStatus())
        		.notiCreationDate(notificationEntity.getNotiCreationDate())
        		.notiSentDate(notificationEntity.getNotiSentDate())
        		.recipient(notificationEntity.getNotiRecipient().getUserId())
        		.sender(notificationEntity.getNotiSender().getUserName())
        		.build();
	}

	
}
