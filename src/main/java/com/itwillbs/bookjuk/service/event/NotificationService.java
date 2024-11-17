package com.itwillbs.bookjuk.service.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.dto.NotiDTO;
import com.itwillbs.bookjuk.entity.event.NotiCheckEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.event.NotiCheckRepository;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notiRepository;
	private final NotiCheckRepository notiCheckRepository;
	
	public Page<NotiDTO> getAllNoti(Long userNum, Pageable pageable) {
		try {
			// 알림 관리 페이지
			if(userNum == 0L) {
				log.info("========================================");
				return notiRepository.findAll(pageable).map(this::convertToDto);
			}else {
			// 유저 알림 페이지
				Page<NotiCheckEntity> notificationChecks = notiCheckRepository.findByNotiRecipient_UserNum(userNum, pageable);
				Page<NotificationEntity> notifications = notiRepository.findByNotiRecipient_UserNum(userNum, pageable);
				
				List<NotiDTO> notiDTO = new ArrayList<>();
				for(int i = 0; i < notificationChecks.getContent().size(); i++) {
					NotiCheckEntity check = notificationChecks.getContent().get(i);
					NotificationEntity notification = notifications.getContent().get(i);
					NotiDTO dto = NotiDTO.builder()
							.notiId(notification.getNotiId())
							.sender(notification.getNotiSender().getUserName())
							.notiContent(notification.getNotiContent())
							.notiSentDate(notification.getNotiSentDate())
							.notiChecked(check.isNotiChecked())
							.build();
					notiDTO.add(dto);
				}
				return new PageImpl<>(notiDTO, pageable, notificationChecks.getTotalElements());
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
	
	private NotiDTO convertToDto2(NotificationEntity notificationEntity) {
        return NotiDTO.builder()
        		.notiId(notificationEntity.getNotiId())
        		.notiRecipient(notificationEntity.getNotiRecipient().getUserNum())
        		.notiSender(notificationEntity.getNotiSender().getUserNum())
        		.notiContent(notificationEntity.getNotiContent())
        		.notiType(notificationEntity.getNotiType())
        		.notiStatus(notificationEntity.getNotiStatus())
        		.notiCreationDate(notificationEntity.getNotiCreationDate())
        		.notiSentDate(notificationEntity.getNotiSentDate())
        		.notiChecked(true)
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
		if(notificationEntity != null) {
			Optional<NotiCheckEntity> entity = notiCheckRepository.findByNotiId_NotiId(notiId);
			if(entity.isPresent()) {
				NotiCheckEntity checkEntity;
				checkEntity = entity.get();
				checkEntity.setNotiChecked(true);
				notiCheckRepository.save(checkEntity);
			}			
		}
		return convertToDto2(notificationEntity);
	}

	public boolean getNotiByIdAndUserNum(Long notiId, Long userNum) {
		return notiRepository.existsByNotiIdAndNotiRecipient_UserNum(notiId, userNum);
	}

	
}
