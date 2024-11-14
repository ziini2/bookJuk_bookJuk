package com.itwillbs.bookjuk.service.rent;

import com.itwillbs.bookjuk.dto.RentDTO;
import com.itwillbs.bookjuk.dto.RentResponseDTO;
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

    // RentEntity -> RentDTO 변환
    private RentDTO toDTO(RentEntity rent) {
        return new RentDTO(
                rent.getRentNum(),
                rent.getUser().getUserId(),
                rent.getUser().getUserName(),
                rent.getUser().getUserPhone(),
                rent.getBook().getBookInfoEntity().getBookName(),
                rent.getBook().getBookInfoEntity().getBookNum(),
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
        Page<RentEntity> rentPage = null;
        List<RentDTO> content = null;

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

                case "userId" -> userRepository.findByUserIdContaining(keyword)
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

                case "userId" -> userRepository.findByUserIdContaining(keyword)
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

                case "userId" -> userRepository.findByUserIdContaining(keyword)
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
            RentEntity rent = rentRepository.findById(rentNum).orElseThrow();
            rent.setRentStatus(true);
            rent.setReturnDate(now());
            rentRepository.save(rent);

            if (rent.getRentEnd().isBefore(now())) {
                log.info("연체료 부과{}", rent.getRentNum());

            }
        });
    }
}
