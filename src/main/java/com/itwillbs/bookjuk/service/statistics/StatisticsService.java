package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.dto.statistics.StatisticsDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsRequestDTO;
import com.itwillbs.bookjuk.dto.statistics.StatisticsResponseDTO;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.repository.PointDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final PointDealRepository pointDealRepository;

    // PointDealEntity -> StatisticsDTO 변환
    private StatisticsDTO toDTO(PointDealEntity pointDeal) {
        return StatisticsDTO.builder()
                .storeName(pointDeal.getRent().getStoreCode().getStoreName().split(" ", 2)[1])
                .pointPayName(pointDeal.getPointPayName())
                .pointPrice(pointDeal.getPointPrice() * -1)
                .rentStart(pointDeal.getRent().getRentStart())
                .rentEnd(pointDeal.getRent().getRentEnd())
                .returnDate(pointDeal.getRent().getReturnDate())
                .overdueDays(pointDeal.getOverdue().getOverdueDays())
                .isbn(pointDeal.getRent().getBook().getBookInfoEntity().getIsbn())
                .genre(pointDeal.getRent().getBook().getBookInfoEntity().getGenre().getGenreName())
                .author(pointDeal.getRent().getBook().getBookInfoEntity().getAuthor())
                .userNum(pointDeal.getRent().getUser().getUserNum())
                .build();
    }

    public StatisticsResponseDTO getRevenueData(StatisticsRequestDTO statisticsRequestDTO) {
        Pageable pageable = PageRequest.of
                (statisticsRequestDTO.getPage(), statisticsRequestDTO.getSize(), Sort.by("reqDate").descending());
        Page<PointDealEntity> pointDealPage;
        List<StatisticsDTO> content;


        LocalDateTime startDate = LocalDateTime.of(statisticsRequestDTO.getStartDate(), LocalTime.from(LocalDateTime.MIN));
        LocalDateTime endDate = LocalDateTime.of(statisticsRequestDTO.getEndDate(), LocalTime.from(LocalDateTime.MAX));
        String pointOption = statisticsRequestDTO.getPointOption();
        String storeName = statisticsRequestDTO.getStoreName();

        if (pointOption.equals("전체")) {
            pointDealPage = pointDealRepository.findAllFirstByReqDateBetweenOrderByReqDateDesc(startDate, endDate, pageable);
        } else  {
            pointDealPage = pointDealRepository.findAllFirstByReqDateBetweenAndPointPayNameOrderByReqDateDesc(startDate, endDate, pointOption, pageable);
        }

        content = pointDealPage.stream().map(this::toDTO).toList();

        if (!storeName.equals("전체")) {
            content = content.stream().filter(statisticsDTO -> statisticsDTO.getStoreName().equals(storeName)).toList();
        }

        return StatisticsResponseDTO.builder()
                .content(content)
                .currentPage(pointDealPage.getNumber())
                .totalPages(pointDealPage.getTotalPages())
                .totalElements(pointDealPage.getTotalElements())
                .build();

    }
}
