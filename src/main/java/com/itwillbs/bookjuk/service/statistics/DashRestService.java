package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.dto.dashboard.TotalStatisticsDTO;
import com.itwillbs.bookjuk.repository.*;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashRestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;
    private final PointDealRepository pointDealRepository;
    private final RentRepository rentRepository;
    private final OverdueRepository overdueRepository;
    private final JdbcTemplate jdbcTemplate;
    private LocalDate now = LocalDate.now();


    public Long getNumbersOfNewCustomers() {

        Timestamp startOfDay = Timestamp.valueOf(now.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(now.plusDays(1).atStartOfDay().minusNanos(1));

        return userRepository.countByCreateDateBetween(startOfDay, endOfDay);
    }

    public Long getNumbersOfEvents() {

        Timestamp now1 = new Timestamp(System.currentTimeMillis());

        return eventRepository.countByStartEventDateBeforeAndEndEventDateAfter(now1, now1);

    }

    public Long getDailyRevenue() {

        LocalDateTime startOfDay = now.atStartOfDay();
        LocalDateTime endOfDay = now.plusDays(1).atStartOfDay().minusNanos(1);

        Optional<Long> revenue = paymentRepository.sumAmountByReqDateBetween(startOfDay, endOfDay);
        return revenue.orElse(0L);
    }

    public Long getDailyPoint() {

        LocalDateTime startOfDay = now.atStartOfDay();
        LocalDateTime endOfDay = now.plusDays(1).atStartOfDay().minusNanos(1);

        Optional<Long> point = pointDealRepository.sumAmountByReqDateBetween(startOfDay, endOfDay);
        return point.orElse(0L) * -1L;
    }

    public Long getDailyRental() {

        Optional<Long> rental = rentRepository.countByRentStatusIsFalse();

        return rental.orElse(0L);
    }

    public long getDailyDelay() {

        Optional<Long> delay = rentRepository.countByRentEndAfterAndRentStatusIsFalse(now);

        return delay.orElse(0L);
    }

    public List<TotalStatisticsDTO> getConsolidatedStatisticsWithFilledDates(String period) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch (period) {
            case "week":
                startDate = LocalDate.now().minusWeeks(1);
                break;
            case "month":
                startDate = LocalDate.now().minusMonths(1);
                break;
            case "quarter":
                startDate = LocalDate.now().minusMonths(3);
                break;
            default:
                break;
        }

        String sql = """
            SELECT date,
                   ifnull(SUM(revenue), 0)     AS revenue,
                   ifnull(SUM(point), 0)       AS point,
                   COUNT(rental)    AS rental,
                   COUNT(delay)     AS delay,
                   COUNT(newUser)   AS newUser
            FROM (
                     SELECT DATE(p.req_date) AS date,
                            p.payment_price  AS revenue,
                            NULL             AS point,
                            NULL             AS rental,
                            NULL             AS delay,
                            NULL             AS newUser
                     FROM payment p
                     WHERE req_date BETWEEN ? AND ?
                     
                     UNION ALL
                     
                     SELECT DATE(pd.req_date) AS date,
                            NULL              AS revenue,
                            pd.point_price * -1   AS point,
                            NULL              AS rental,
                            NULL              AS delay,
                            NULL              AS newUser
                     FROM point_deal pd
                     WHERE point_pay_status = 'SUCCESSFUL'
                       AND point_pay_name IN ('대여료', '연체료')
                       AND req_date BETWEEN ? AND ?
                     
                     UNION ALL
                     
                     SELECT DATE(r.rent_start) AS date,
                            NULL               AS revenue,
                            NULL               AS point,
                            r.rent_num         AS rental,
                            NULL               AS delay,
                            NULL               AS newUser
                     FROM rent r
                     WHERE rent_start BETWEEN ? AND ?
                     
                     UNION ALL
                     
                     SELECT DATE(o.over_start) AS date,
                            NULL               AS revenue,
                            NULL               AS point,
                            NULL               AS rental,
                            o.over_num         AS delay,
                            NULL               AS newUser
                     FROM overdue o
                     WHERE over_start BETWEEN ? AND ?
                     
                     UNION ALL
                     
                     SELECT DATE(u.create_date) AS date,
                            NULL                AS revenue,
                            NULL                AS point,
                            NULL                AS rental,
                            NULL                AS delay,
                            u.user_num          AS newUser
                     FROM users u
                     WHERE create_date BETWEEN ? AND ?
                 ) AS t
            GROUP BY date
            ORDER BY date;
            """;

        List<TotalStatisticsDTO> statisticsList = jdbcTemplate.query(
                sql,
                new Object[]{startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate},
                (rs, rowNum) -> TotalStatisticsDTO.builder()
                        .date(rs.getString("date"))
                        .revenue(rs.getLong("revenue"))
                        .point(rs.getLong("point"))
                        .rental(rs.getLong("rental"))
                        .delay(rs.getLong("delay"))
                        .newUser(rs.getLong("newUser"))
                        .build()
        );

        // Fill missing dates with zero values
        Map<LocalDate, TotalStatisticsDTO> statisticsMap = new HashMap<>();
        for (TotalStatisticsDTO stat : statisticsList) {
            statisticsMap.put(LocalDate.parse(stat.getDate()), stat);
        }

        List<TotalStatisticsDTO> filledStatisticsList = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (statisticsMap.containsKey(date)) {
                filledStatisticsList.add(statisticsMap.get(date));
            } else {
                filledStatisticsList.add(TotalStatisticsDTO.builder()
                        .date(date.toString())
                        .revenue(0L)
                        .point(0L)
                        .rental(0L)
                        .delay(0L)
                        .newUser(0L)
                        .build());
            }
        }

        return filledStatisticsList;
    }





}
