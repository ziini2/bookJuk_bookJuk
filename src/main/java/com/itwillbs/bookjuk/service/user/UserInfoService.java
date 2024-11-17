package com.itwillbs.bookjuk.service.user;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.dto.*;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.PointDealRepository;
import com.itwillbs.bookjuk.repository.RentRepository;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final RentRepository rentRepository;
    private final PointDealRepository pointDealRepository;


    //유저pk 로 유저 정보 가져오기
    public UserInfoDTO getUserInfo(Long userNum) {
        log.info("getUserInfo userNum={}", userNum);
        //여기서 가져와서 판단한번 하고 보내자 ..
        UserInfoDTO getUserDTO = toUserInfoDTO(userRepository.findByUserNum(userNum), userContentRepository.findByUserEntity_UserNum(userNum));
        log.info("getUserInfo getUserDTO={}", getUserDTO.toString());
        if (getUserDTO.getLoginType() == LoginType.MANUAL){
            return getUserDTO;
        }
        getUserDTO.setUserId(getUserDTO.getLoginType().toString());
        return getUserDTO;
    }

    //userEntity -> UserDTO 변환 메서드
    public UserInfoDTO toUserInfoDTO(UserEntity userEntity, UserContentEntity userContentEntity) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserNum(userEntity.getUserNum());
        userInfoDTO.setUserId(userEntity.getUserId());
        userInfoDTO.setUserName(userEntity.getUserName());
        userInfoDTO.setUserBirthday(userEntity.getUserBirthday());
        userInfoDTO.setUserEmail(userEntity.getUserEmail());
        userInfoDTO.setUserPhone(userEntity.getUserPhone());
        userInfoDTO.setLoginType(userEntity.getLoginType());
        userInfoDTO.setUserGender(userEntity.getUserGender());

        // UserContentEntity 정보 설정 (null 체크)
        if (userContentEntity != null) {
            userInfoDTO.setUserPoint(userContentEntity.getUserPoint());
            userInfoDTO.setBringBook(userContentEntity.getBringBook());
        } else {
            log.warn("UserContentEntity is null for userNum={}", userEntity.getUserNum());
            userInfoDTO.setUserPoint(0); // 기본값 설정
            userInfoDTO.setBringBook(0); // 기본값 설정
        }
        return userInfoDTO;
    }


    //책 대여 정보 가져오기
    public RentPaginationDTO getBookRentInfo(Long userNum, int pageSize, int rentPage) {
        log.info("getBookRentInfo userNum={}", userNum);
        Pageable pageable = PageRequest.of(rentPage, pageSize, Sort.by("rentStart").descending());
        Page<RentEntity> rentEntityPage = rentRepository.findByUser_UserNum(userNum, pageable);
        return toRentPagination(rentEntityPage);
    }

    //UserPaginationDTO 로 변환 메서드
    public RentPaginationDTO toRentPagination(Page<RentEntity> rentEntityList) {
        List<BookRentInfoDTO> bookRentInfoDTOList = rentEntityList.stream().map(rent -> BookRentInfoDTO.builder()
                .rentStart(rent.getRentStart())
                .rentEnd(rent.getRentEnd())
                .rentPrice(rent.getRentPrice())
                .rentStatus(rent.getRentStatus())
                .storeName(rent.getStoreCode().getStoreName())
                .bookName(rent.getBook().getBookInfoEntity().getBookName())
                .build()).toList();

        return RentPaginationDTO.builder()
                .items(bookRentInfoDTOList)
                .totalPages(rentEntityList.getTotalPages())
                .currentPage(rentEntityList.getNumber())
                .hasNext(rentEntityList.hasNext())
                .hasPrevious(rentEntityList.hasPrevious())
                .build();
    }


    //포인트 사용 정보 가져오기
    public PointPaginationDTO getPointInfo(Long userNum, int pageSize, int pointPage) {
        log.info("getPointInfo userNum={}", userNum);
        Pageable pageable = PageRequest.of(pointPage, pageSize, Sort.by("reqDate").descending());
        Page<PointDealEntity> pointDealEntityPage = pointDealRepository.findByUserContentEntity_MemberNum(userNum, pageable);
        log.info("getPointInfo pointDealEntityPage={}", pointDealEntityPage.toString());
        return toPointPagination(pointDealEntityPage);
    }

    //PointPaginationDTO 로 변환 메서드
    public PointPaginationDTO toPointPagination(Page<PointDealEntity> pointDealEntity) {
        List<PointInfoDTO> pointInfoDTOList = pointDealEntity.stream().map(point -> PointInfoDTO.builder()
                .reqDate(point.getReqDate().toString())
                .pointPrice(point.getPointPrice())
                .pointPayName(point.getPointPayName())
                .build()).toList();

        return PointPaginationDTO.builder()
                .items(pointInfoDTOList)
                .totalPages(pointDealEntity.getTotalPages())
                .currentPage(pointDealEntity.getNumber())
                .hasNext(pointDealEntity.hasNext())
                .hasPrevious(pointDealEntity.hasPrevious())
                .build();
    }
}


