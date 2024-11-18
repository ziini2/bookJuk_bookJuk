package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.domain.login.UserRole;
import com.itwillbs.bookjuk.dto.UserDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.event.CouponEntity;
import com.itwillbs.bookjuk.entity.event.EventConditionEntity;
import com.itwillbs.bookjuk.entity.event.EventCountEntity;
import com.itwillbs.bookjuk.entity.event.NotificationEntity;
import com.itwillbs.bookjuk.exception.ValidationException;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.CouponRepository;
import com.itwillbs.bookjuk.repository.event.EventConditionRepository;
import com.itwillbs.bookjuk.repository.event.EventCountRepository;
import com.itwillbs.bookjuk.repository.event.NotificationRepository;
import com.itwillbs.bookjuk.service.event.EventService;
import com.itwillbs.bookjuk.service.event.NotificationService;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EventConditionRepository eventConditionRepository;
    private final EventService eventService;
    private final NotificationService notificationService;


    //회원가입 프로세스
    public boolean joinProcess(UserDTO userDTO){
        //UserDTO 유효성 검사
        validateUser(userDTO);

        //DTO를 엔티티로 변환
        UserEntity userEntity = toEntity(userDTO);

        //비밀번호 암호화 후 저장 (단방향 해쉬 암호화)
        userEntity.setUserPassword(bCryptPasswordEncoder.encode(userDTO.getUserPassword()));

        //저장된 유저 반환
        UserEntity saveUser = userRepository.save(userEntity);

        //userContent 테이블에 유저 등록
        UserContentEntity userContentEntity = new UserContentEntity();
        userContentEntity.setUserEntity(saveUser);
        UserContentEntity saveUserContent = userContentRepository.save(userContentEntity);
        
        //회원 가입시 가입인사 알림 발송(김주완)
        notificationService.firstNoti(saveUser);

        //저장 성공 여부 확인
        if(saveUser != null) {
        	
        	// event_condition 테이블에서 event_is_active = true 이면서 event_condition_type = "신규 가입" 인 데이터 조회 
        	List<EventConditionEntity> resultList = eventConditionRepository.findByEventIsActiveTrueAndEventConditionType("신규 가입");
        	
        	// 조건에 맞는 데이터가 있으면 
        	if(!resultList.isEmpty()) {
        		
        		// event_count, coupon, notification 데이터 생성
        		eventService.createEventEntitiesForUser(resultList, saveUser);
        	}
        	return true;
        }else {
        	return false;
        }
    }    

    //UserDTO -> UserEntity 변환 메서드
    public UserEntity toEntity(UserDTO userDTO){
        return UserEntity.builder()
                .userId(userDTO.getUserId())
                .userPassword(userDTO.getUserPassword())
                .userName(userDTO.getUserName())
                .userGender(userDTO.getUserGender())
                .userBirthday(userDTO.getUserBirthday())
                .userEmail(userDTO.getUserEmail())
                .userPhone(userDTO.getUserPhone())
                .userRole(UserRole.ROLE_USER)
                .loginType(LoginType.MANUAL)
                .accepted(userDTO.isAccepted())
                .activate(true)
                .build();
    }

    //유효성 검사 메서드
    private void validateUser(UserDTO userDTO) {
        //아이디 중복 검사
        if (userRepository.existsByUserId(userDTO.getUserId())) {
            log.warn("중복된 아이디로 가입 시도: {}", userDTO.getUserId());
            throw new ValidationException("이미 사용 중인 아이디 입니다.");
        }

        //이메일 중복 검사
        if (userRepository.existsByUserEmail(userDTO.getUserEmail())) {
            log.warn("중복된 이메일로 가입 시도: {}", userDTO.getUserEmail());
            throw new ValidationException("이미 등록된 이메일입니다.");
        }

        //비밀번호 길이 검사
        if (userDTO.getUserPassword().length() < 8) {
            throw new ValidationException("비밀번호는 최소 8자 이상이어야 합니다.");
        }

        //이메일 형식 검사
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!Pattern.matches(emailPattern, userDTO.getUserEmail())) {
            throw new ValidationException("유효하지 않은 이메일 형식입니다.");
        }
    }

    //소셜로그인 추가정보 저장
    public boolean saveUserAddInfo(Long userNum, UserDTO userDTO) {
        log.info("saveUserPhone: {}", userNum);
        UserEntity user = userRepository.findByUserNum(userNum);
        if (user == null) {
            return false;
        }
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserBirthday(userDTO.getUserBirthday());
        user.setUserGender(userDTO.getUserGender());
        user.setUserRole(UserRole.ROLE_USER);
        UserEntity updateUser = userRepository.save(user);
        return SecurityUtil.reloadUserRole(updateUser);
    }

    //비밀변호 변경 저장
    public boolean saveUserPassword(Long userNum, String password) {
        UserEntity user = userRepository.findByUserNum(userNum);
        if (user == null) {
            return false;
        }
        user.setUserPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }
}
