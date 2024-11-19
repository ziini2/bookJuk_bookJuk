package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.controller.statistics.StatisticsRestController;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final PointDealRepository pointDealRepository;
    private final JdbcTemplate jdbcTemplate;

    // PointDealEntity -> StatisticsDTO 변환
    private StatisticsDTO toDTO(PointDealEntity pointDeal) {
        return StatisticsDTO.builder()
                .storeName(pointDeal.getRent().getStoreCode().getStoreName())
                .pointPayName(pointDeal.getPointPayName())
                .pointPrice(pointDeal.getPointPrice() * -1)
                .rentStart(pointDeal.getRent().getRentStart())
                .rentEnd(pointDeal.getRent().getRentEnd())
                .returnDate(pointDeal.getRent().getReturnDate())
                .overdueDays(pointDeal.getOverdue() == null ? 0 : pointDeal.getOverdue().getOverdueDays())
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
        String genre = statisticsRequestDTO.getGenre();
        String storeName = statisticsRequestDTO.getStoreName();

        // 포인트 항목별 필터
        if (pointOption.equals("전체")) {
            pointDealPage = pointDealRepository.findAllByReqDateBetweenAndPointPayNameInOrderByReqDateDescPage(startDate, endDate, List.of("대여료", "연체료"), pageable);
        } else  {
            pointDealPage = pointDealRepository.findAllFirstByReqDateBetweenAndPointPayNameOrderByReqDateDesc(startDate, endDate, pointOption, pageable);
        }

        content = pointDealPage.stream().map(this::toDTO).toList();

        // 지점별 필터
        if (!storeName.equals("전체")) {
            content = content.stream().filter(statisticsDTO -> statisticsDTO.getStoreName().split(" ", 2)[1].equals(storeName)).toList();
        }

        // 장르별 필터
        if (!genre.equals("전체")) {
            content = content.stream().filter(statisticsDTO -> statisticsDTO.getGenre().trim().equals(genre)).toList();
        }


        return StatisticsResponseDTO.builder()
                .content(content)
                .currentPage(pointDealPage.getNumber())
                .totalPages(pointDealPage.getTotalPages())
                .totalElements(pointDealPage.getTotalElements())
                .build();

    }

    public StatisticsResponseDTO getCustomerData(StatisticsRestController.StatisticsCustomerRequestDTO statisticsCustomerRequestDTO) {

        LocalDateTime startDate = LocalDateTime.of(statisticsCustomerRequestDTO.startDate(), LocalTime.from(LocalDateTime.MIN));
        LocalDateTime endDate = LocalDateTime.of(statisticsCustomerRequestDTO.endDate(), LocalTime.from(LocalDateTime.MAX));
        int page = statisticsCustomerRequestDTO.page();
        int size = statisticsCustomerRequestDTO.size();

        String sql = """
        WITH RentalSummary AS (
            SELECT
                u.user_num,
                u.create_date AS create_date,
                SUM(CASE WHEN p.point_pay_name = '대여료' THEN p.point_price ELSE 0 END) AS sum_rental_fee,
                SUM(CASE WHEN p.point_pay_name = '연체료' THEN p.point_price ELSE 0 END) AS sum_overdue_fee,
                SUM(CASE WHEN p.point_pay_name = '충전' THEN p.point_price ELSE 0 END) AS sum_recharge,
                SUM(CASE WHEN p.point_pay_name = '쿠폰' THEN p.point_price ELSE 0 END) AS sum_coupon,
                u.user_gender,
                substring(u.user_birthday, 1, 4) AS user_birthday,
                sum(o.overdue_days) as overdue_days,
                COUNT(o.overdue_days) AS overdue_count,
                COUNT(p.rent_num) AS rent_count
            FROM
                point_deal p
                    LEFT JOIN user_content uc ON p.member_num = uc.member_num
                    LEFT JOIN users u ON uc.user_num = u.user_num
                    LEFT JOIN overdue o ON p.over_num = o.over_num
            WHERE
                p.point_pay_name IN ('대여료', '연체료', '충전', '쿠폰')
               And p.req_date BETWEEN ? AND ?
            GROUP BY
                u.user_num,
                u.create_date,
                u.user_gender,
                substring(u.user_birthday, 1, 4)
        )
        SELECT *
        FROM RentalSummary
        ORDER BY sum_recharge DESC
        LIMIT ? OFFSET ?
        """;

        List<StatisticsDTO> statisticsDTOS = jdbcTemplate.query(
                sql, new Object[]{startDate, endDate, size, page * size}, (rs, rowNum) -> StatisticsDTO.builder()
                        .num(rowNum)
                        .userNum(rs.getLong("user_num"))
                        .joinDate(rs.getDate("create_date").toLocalDate())
                        .totalRentPrice(rs.getLong("sum_rental_fee") * -1)
                        .totalOverduePrice(rs.getLong("sum_overdue_fee") * -1)
                        .totalPaymentPrice(rs.getLong("sum_recharge"))
                        .totalCouponPrice(rs.getLong("sum_coupon"))
                        .gender(rs.getString("user_gender").equals("male") ? "남" : "여")
                        .age(LocalDate.now().getYear() - Integer.parseInt(rs.getString("user_birthday")))
                        .totalRentDays(rs.getLong("rent_count") * 5)
                        .totalRentCount(rs.getLong("rent_count"))
                        .totalOverdueDays(rs.getLong("overdue_days"))
                        .totalOverdueCount(rs.getLong("overdue_count"))
                        .build()
        );


        return StatisticsResponseDTO.builder()
                .content(statisticsDTOS)
                .currentPage(page)
                .totalPages(1)
                .totalElements(statisticsDTOS.size())
                .build();
    }

    public List<StatisticsDTO> getCustomerAllData(StatisticsRestController.StatisticsCustomerRequestDTOPage statisticsCustomerRequestDTO) {

        LocalDateTime startDate = LocalDateTime.of(statisticsCustomerRequestDTO.startDate(), LocalTime.from(LocalDateTime.MIN));
        LocalDateTime endDate = LocalDateTime.of(statisticsCustomerRequestDTO.endDate(), LocalTime.from(LocalDateTime.MAX));

        String sql = """
        WITH RentalSummary AS (
            SELECT
                u.user_num,
                u.create_date AS create_date,
                SUM(CASE WHEN p.point_pay_name = '대여료' THEN p.point_price ELSE 0 END) AS sum_rental_fee,
                SUM(CASE WHEN p.point_pay_name = '연체료' THEN p.point_price ELSE 0 END) AS sum_overdue_fee,
                SUM(CASE WHEN p.point_pay_name = '충전' THEN p.point_price ELSE 0 END) AS sum_recharge,
                SUM(CASE WHEN p.point_pay_name = '쿠폰' THEN p.point_price ELSE 0 END) AS sum_coupon,
                u.user_gender,
                substring(u.user_birthday, 1, 4) AS user_birthday,
                sum(o.overdue_days) as overdue_days,
                COUNT(o.overdue_days) AS overdue_count,
                COUNT(p.rent_num) AS rent_count
            FROM
                point_deal p
                    LEFT JOIN user_content uc ON p.member_num = uc.member_num
                    LEFT JOIN users u ON uc.user_num = u.user_num
                    LEFT JOIN overdue o ON p.over_num = o.over_num
            WHERE
                p.point_pay_name IN ('대여료', '연체료', '충전', '쿠폰')
               And p.req_date BETWEEN ? AND ?
            GROUP BY
                u.user_num,
                u.create_date,
                u.user_gender,
                substring(u.user_birthday, 1, 4)
        )
        SELECT *
        FROM RentalSummary
        ORDER BY sum_recharge DESC
        """;

        return jdbcTemplate.query(
                sql, new Object[]{startDate, endDate}, (rs, rowNum) -> StatisticsDTO.builder()
                        .num(rowNum)
                        .userNum(rs.getLong("user_num"))
                        .joinDate(rs.getDate("create_date").toLocalDate())
                        .totalRentPrice(rs.getLong("sum_rental_fee") * -1)
                        .totalOverduePrice(rs.getLong("sum_overdue_fee") * -1)
                        .totalPaymentPrice(rs.getLong("sum_recharge"))
                        .totalCouponPrice(rs.getLong("sum_coupon"))
                        .gender(rs.getString("user_gender").equals("male") ? "남" : "여")
                        .age(LocalDate.now().getYear() - Integer.parseInt(rs.getString("user_birthday")))
                        .totalRentDays(rs.getLong("rent_count") * 5)
                        .totalRentCount(rs.getLong("rent_count"))
                        .totalOverdueDays(rs.getLong("overdue_days"))
                        .totalOverdueCount(rs.getLong("overdue_count"))
                        .build()
        );

    }

    public List<StatisticsDTO> getRevenueAllData(StatisticsRequestDTO statisticsRequestDTO) {

        Optional<List<PointDealEntity>> pointDealList;
        List<StatisticsDTO> content;


        LocalDateTime startDate = LocalDateTime.of(statisticsRequestDTO.getStartDate(), LocalTime.from(LocalDateTime.MIN));
        LocalDateTime endDate = LocalDateTime.of(statisticsRequestDTO.getEndDate(), LocalTime.from(LocalDateTime.MAX));
        String pointOption = statisticsRequestDTO.getPointOption();
        String genre = statisticsRequestDTO.getGenre();
        String storeName = statisticsRequestDTO.getStoreName();

        // 포인트 항목별 필터
        if (pointOption.equals("전체")) {
            pointDealList = pointDealRepository.findAllByReqDateBetweenAndPointPayNameInOrderByReqDateDesc(startDate, endDate, List.of("대여료", "연체료"));
        } else  {
            pointDealList = pointDealRepository.findAllFirstByReqDateBetweenAndPointPayNameOrderByReqDateDesc(startDate, endDate, pointOption);
        }

        if (pointDealList.isEmpty()) {
            return List.of();
        }

        content = pointDealList.get().stream().map(this::toDTO).toList();

        // 지점별 필터
        if (!storeName.equals("전체")) {
            content = content.stream().filter(statisticsDTO -> statisticsDTO.getStoreName().split(" ", 2)[1].equals(storeName)).toList();
        }

        // 장르별 필터
        if (!genre.equals("전체")) {
            content = content.stream().filter(statisticsDTO -> statisticsDTO.getGenre().trim().equals(genre)).toList();
        }

        return content;



    }
}