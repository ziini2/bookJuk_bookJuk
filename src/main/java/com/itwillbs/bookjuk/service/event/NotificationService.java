package com.itwillbs.bookjuk.service.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.dto.NotiDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.NotiCheckEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
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
	private final UserRepository userRepository;
	
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

	// 알림 수동 전송
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendNotifications(List<Long> selectedRecipients, String notiContent, Long userNum) {
		int batchSize = 100; // 100개씩 처리
		int totalSize = selectedRecipients.size(); // 전체 데이터 수
		
		for(int i = 0; i < totalSize; i += batchSize) {
			List<Long> batchList = selectedRecipients.subList(i, Math.min(i + batchSize, totalSize));
			processBatch(batchList, notiContent, userNum); // 나눠진 데이터를 처리하는 메서드 호출
		}
	}

	// 알림 전송할 데이터를 나눠서 보내기
	private void processBatch(List<Long> batchList, String notiContent, Long userNum) {
		List<NotificationEntity> notifications = new ArrayList<>();
		// 관리자 아이디 조회
		UserEntity manager = getManager(userNum);
	    for (Long recipient : batchList) {
	    	UserEntity user = userRepository.findByUserNum(recipient);
	    	NotificationEntity notification = NotificationEntity.builder()
	    			.notiContent(notiContent)
	    			.notiCreationDate(new Timestamp(System.currentTimeMillis()))
	    			.notiRecipient(user)
	    			.notiSender(manager)
	    			.notiSentDate(new Timestamp(System.currentTimeMillis()))
	    			.notiType("쪽지")
	    			.build();
	    	NotiCheckEntity notiCheckEntity = NotiCheckEntity.builder()
					.notiId(notification)
					.notiRecipient(user)
					.notiChecked(false)
					.build();
			notiCheckRepository.save(notiCheckEntity);
	        try {
	        	if (user == null) {
	                throw new IllegalArgumentException("사용자 정보를 찾을 수 없음: " + recipient);
	            }
	            notification.setNotiStatus("성공"); // 성공 상태 설정
	        } catch (Exception e) {
	            notification.setNotiStatus("실패");	            
	            System.err.println("알림 전송 실패: " + recipient + ", 이유: " + e.getMessage());
	        }
	        notifications.add(notification);
	    }
	    notiRepository.saveAll(notifications);
	}
	
	public UserEntity getManager(Long userNum) {
		return userRepository.findByUserNum(userNum);
	}
	
	public void firstNoti(UserEntity user) {
		UserEntity manager = getManager(10L);
		NotificationEntity notification = NotificationEntity.builder()
				.notiRecipient(user)
				.notiSender(manager)
				.notiContent("신규 가입을 축하드립니다!")
				.notiType("쪽지")
				.notiStatus("성공")
				.notiCreationDate(new Timestamp(System.currentTimeMillis()))
				.notiSentDate(new Timestamp(System.currentTimeMillis()))
        		.build();
		notiRepository.save(notification);
		NotiCheckEntity notiCheckEntity = NotiCheckEntity.builder()
				.notiId(notification)
				.notiRecipient(user)
				.notiChecked(false)
				.build();
		notiCheckRepository.save(notiCheckEntity);
	}

	
}
