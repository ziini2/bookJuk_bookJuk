package com.itwillbs.bookjuk.service.rent;

import com.itwillbs.bookjuk.domain.pay.PointPayStatus;
import com.itwillbs.bookjuk.dto.rent.RentDTO;
import com.itwillbs.bookjuk.dto.rent.RentResponseDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.entity.rent.Overdue;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final BookInfoRepository bookInfoRepository;
    private final OverdueRepository overdueRepository;
    private final UserContentRepository userContentRepository;
    private final PointDealRepository pointDealRepository;

    // RentEntity -> RentDTO 변환
    private RentDTO toDTO(RentEntity rent) {
        return new RentDTO(
                rent.getRentNum(),
                rent.getUser().getUserId(),
                rent.getUser().getUserName(),
                rent.getUser().getUserPhone(),
                rent.getBook().getBooksId(),
                rent.getBook().getBookInfoEntity().getIsbn(),
                rent.getBook().getBookInfoEntity().getBookName(),
                rent.getStoreCode().getStoreName(),
                rent.getRentStart(),
                rent.getRentEnd(),
                rent.getReturnDate(),
                rent.getRentStatus() ? "반납 완료" : "대여 중"
        );
    }

    // 전체 RentEntity를 RentResponseDTO로 반환
    public RentResponseDTO findAllWithDTO(Boolean rented, Boolean returned, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rentNum").descending());
        Page<RentEntity> rentPage;
        List<RentDTO> content;

        if (rented && returned) {
            rentPage = rentRepository.findAll(pageable);
            content = rentPage.getContent().stream().map(this::toDTO).toList();
        } else if (!rented) {
            rentPage = rentRepository.findAllByRentStatusIsTrue(pageable);
            content = rentPage.getContent().stream().map(this::toDTO).toList();
        } else {
            rentPage = rentRepository.findAllByRentStatusIsFalse(pageable);
            content = rentPage.getContent().stream().map(this::toDTO).toList();
        }

        return new RentResponseDTO(content, rentPage.getNumber(), rentPage.getTotalPages(), rentPage.getTotalElements());
    }

    // 검색 조건에 따라 RentEntity를 RentResponseDTO로 반환
    public RentResponseDTO findAllBySearchWithDTO(
            String criteria, String keyword, Boolean rented, Boolean returned, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rentNum").descending());

        Page<RentEntity> rentPage = null;

        if (rented && returned) {
            rentPage = switch (criteria) {
                case "userName" -> userRepository.findAllByUserNameContaining(keyword)
                        .map(userEntities -> rentRepository.findAllByUserIn(userEntities, pageable))
                        .orElse(Page.empty());

                case "bookName" -> bookInfoRepository.findAllByBookNameContaining(keyword)
                        .flatMap(booksRepository::findAllByBookInfoEntityIn)
                        .map(booksEntities -> rentRepository.findAllByBookIn(booksEntities, pageable))
                        .orElse(Page.empty());

                case "userPhone" -> userRepository.findByUserPhoneContaining(keyword)
                        .map(userEntity -> rentRepository.findAllByUserIn(userEntity, pageable))
                        .orElse(Page.empty());

                default -> Page.empty();
            };
        } else if (!rented) {
            rentPage = switch (criteria) {
                case "userName" -> userRepository.findAllByUserNameContaining(keyword)
                        .map(userEntities -> rentRepository.findAllByUserInAndRentStatusIsTrue(userEntities, pageable))
                        .orElse(Page.empty());

                case "bookName" -> bookInfoRepository.findAllByBookNameContaining(keyword)
                        .flatMap(booksRepository::findAllByBookInfoEntityIn)
                        .map(booksEntities -> rentRepository.findAllByBookInAndRentStatusIsTrue(booksEntities, pageable))
                        .orElse(Page.empty());

                case "userPhone" -> userRepository.findByUserPhoneContaining(keyword)
                        .map(userEntity -> rentRepository.findAllByUserInAndRentStatusIsTrue(userEntity, pageable))
                        .orElse(Page.empty());

                default -> Page.empty();
            };
        } else {
            rentPage = switch (criteria) {
                case "userName" -> userRepository.findAllByUserNameContaining(keyword)
                        .map(userEntities -> rentRepository.findAllByUserInAndRentStatusIsFalse(userEntities, pageable))
                        .orElse(Page.empty());

                case "bookName" -> bookInfoRepository.findAllByBookNameContaining(keyword)
                        .flatMap(booksRepository::findAllByBookInfoEntityIn)
                        .map(booksEntities -> rentRepository.findAllByBookInAndRentStatusIsFalse(booksEntities, pageable))
                        .orElse(Page.empty());

                case "userPhone" -> userRepository.findByUserPhoneContaining(keyword)
                        .map(userEntity -> rentRepository.findAllByUserInAndRentStatusIsFalse(userEntity, pageable))
                        .orElse(Page.empty());

                default -> Page.empty();
            };
        }


        List<RentDTO> content = rentPage.getContent().stream().map(this::toDTO).collect(Collectors.toList());


        return new RentResponseDTO(content, rentPage.getNumber(), rentPage.getTotalPages(), rentPage.getTotalElements());
    }

    @Transactional
    public void returnBook(List<Long> rentNums) {
        rentNums.forEach(rentNum -> {
            // 1. rent 테이블에 반납일 추가
            RentEntity rent = rentRepository.findById(rentNum).orElseThrow();
            rent.setRentStatus(true);
            rent.setReturnDate(now());
            rentRepository.save(rent);

            // 2. Books 테이블에 대여 가능으로 변경
            BooksEntity book = rent.getBook();
            book.setRentStatus(false);
            booksRepository.save(book);

            // 연체시
            // 1. overdue 테이블에 연체 기록 추가
            // 2. PointDeal 테이블에 연체료 추가
            // 3. UserContent 포인트 차감
            if (rent.getRentEnd().isBefore(now())) {
                log.info("연체료 부과{}", rent.getRentNum());
                int perDay = 500;
                LocalDate now = now();
                int overdue_days = (int) (now.toEpochDay() - rent.getRentEnd().toEpochDay()) * -1;
                int overPrice = overdue_days * perDay * -1;

                // 1. overdue 테이블에 연체 기록 추가
                Overdue overdue = Overdue.builder()
                        .rent(rent)
                        .overdueDays(overdue_days)
                        .overPrice(overPrice)
                        .overStart(rent.getRentEnd().plusDays(1))
                        .overEnd(now)
                        .build();

                overdueRepository.save(overdue);

                // 2. PointDeal 테이블에 연체료 추가
                long userNum = rent.getUser().getUserNum();
                UserContentEntity member = userContentRepository.findByUserEntity_UserNum(userNum);
                PointDealEntity pointDeal = PointDealEntity.builder()
                        .userContentEntity(member)
                        .pointPrice(overPrice)
                        .pointPayStatus(PointPayStatus.SUCCESSFUL)
                        .reqDate(LocalDateTime.now())
                        .pointPayName("연체료")
                        .rent(rent)
                        .overdue(overdue)
                        .build();

                pointDealRepository.save(pointDeal);

                // 3. UserContent 포인트 차감
                member.setUserPoint(member.getUserPoint() + overPrice);
                userContentRepository.save(member);
            }
        });
    }


}
