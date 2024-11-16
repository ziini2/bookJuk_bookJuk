package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.dto.dashboard.CategoryData;
import com.itwillbs.bookjuk.dto.dashboard.PointResponseDTO;
import com.itwillbs.bookjuk.dto.dashboard.TotalStatisticsDTO;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.repository.*;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashRestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;
    private final PointDealRepository pointDealRepository;
    private final RentRepository rentRepository;
    private final StoreRepository storeRepository;
    private final JdbcTemplate jdbcTemplate;
    private final LocalDate now = LocalDate.now();


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

        startDate = getStartDate(period, startDate);

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

        // 날짜가 없는 경우 0으로 채워서 반환
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

    private static LocalDate getStartDate(String period, LocalDate startDate) {
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
        return startDate;
    }


    public List<String> getStores() {
        List<StoreEntity> storelist = storeRepository.findAllStoreNameByStoreStatus("open");
        return storelist.stream().map(StoreEntity::getStoreName).map(name -> {
            return name.split(" ", 2)[1];
        }).sorted().toList();
    }

    public PointResponseDTO getPointStatistics(String period) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        startDate = getStartDate(period, startDate);

        Optional<List<PointDealEntity>> genderDate = pointDealRepository.findAllFirstByReqDateBetweenOrderByReqDateDesc(
                LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime()), LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime()));

        List<CategoryData> genderData = new ArrayList<>();
        List<CategoryData> ageData = new ArrayList<>();
        List<CategoryData> genreData = new ArrayList<>();
        List<CategoryData> storeData = new ArrayList<>();

        if (genderDate.isPresent()) {
            for (PointDealEntity pointDealEntity : genderDate.get()) {

                // 성별 포인트 사용 내역
                String genderCategory = pointDealEntity.getUserContentEntity().getUserEntity().getUserGender().equals("mail") ? "남성" : "여성";
                genderData.add(getCategoryData(genderCategory, pointDealEntity));

                // 나이별 포인트 사용 내역
                String birthday = pointDealEntity.getUserContentEntity().getUserEntity().getUserBirthday();
                int ageNum = now.getYear() - Integer.parseInt(birthday.substring(0, 4)) + 1;

                String ageCategory = getAgeCategory(ageNum);

                ageData.add(getCategoryData(ageCategory, pointDealEntity));

                // 장르별 포인트 사용 내역
                String genre = pointDealEntity.getRent().getBook().getBookInfoEntity().getGenre().getGenreName();

                genreData.add(getCategoryData(genre, pointDealEntity));

                // 지점별 포인트 사용 내역
                String store = pointDealEntity.getRent().getStoreCode().getStoreName().split(" ", 2)[1];

                storeData.add(getCategoryData(store, pointDealEntity));

            }
        }

        // 중복된 카테고리들은 합쳐서 반환 및 대여료와 연체료 부호 +로 변경
        genderData = changeSign(mergeCategoryData(genderData));
        ageData = changeSign(mergeCategoryData(ageData));
        genreData = changeSign(mergeCategoryData(genreData));
        storeData = changeSign(mergeCategoryData(storeData));



        return PointResponseDTO.builder()
                .gender(genderData)
                .age(ageData)
                .genre(genreData)
                .store(storeData)
                .build();

    }

    private List<CategoryData> changeSign(List<CategoryData> data) {
        return data.stream().map(categoryData -> {
            categoryData.setRentalFee(categoryData.getRentalFee() * -1);
            categoryData.setOverdueFee(categoryData.getOverdueFee() * -1);
            return categoryData;
        }).toList();
    }

    private List<CategoryData> mergeCategoryData(List<CategoryData> data) {
        List<CategoryData> mergedData = new ArrayList<>();
        // rentalfee와 overduefee를 각각의 변수로 합산
        for (CategoryData categoryData : data) {
            String category = categoryData.getCategory();
            long rentalFee = categoryData.getRentalFee();
            long overdueFee = categoryData.getOverdueFee();

            if (mergedData.stream().anyMatch(data1 -> data1.getCategory().equals(category))) {
                CategoryData merged = mergedData.stream().filter(data1 -> data1.getCategory().equals(category)).findFirst().get();
                merged.setRentalFee(merged.getRentalFee() + rentalFee);
                merged.setOverdueFee(merged.getOverdueFee() + overdueFee);
            } else {
                mergedData.add(categoryData);
            }
        }
        return mergedData;
    }

    @NotNull
    private static String getAgeCategory(int ageNum) {
        return switch (ageNum / 10) {
            case 0, 1 -> "10대";
            case 2 -> "20대";
            case 3 -> "30대";
            case 4 -> "40대";
            case 5 -> "50대";
            default -> "60대 이상";
        };
    }

    public CategoryData getCategoryData(String category, PointDealEntity pointDealEntity) {

        return CategoryData.builder()
                .category(category)
                .rentalFee(pointDealEntity.getPointPayName().equals("대여료") ? pointDealEntity.getPointPrice() : 0L)
                .overdueFee(pointDealEntity.getPointPayName().equals("연체료") ? pointDealEntity.getPointPrice() : 0L)
                .build();
    }
}
