package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.dto.dashboard.*;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.pay.PaymentEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.entity.rent.Overdue;
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
    private final OverdueRepository overdueRepository;
    private final GenreRepository genreRepository;
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




    public PointResponseDTO getPointStatistics(String period, List<String> storeList, String salesOption) {
        LocalDate startDate = now;
        LocalDate endDate = LocalDate.now();

        startDate = getStartDate(period, startDate);

        List<String> pointOptions = List.of("대여료", "연체료");

        Optional<List<PointDealEntity>> rawData = pointDealRepository.findAllByReqDateBetweenAndPointPayNameInOrderByReqDateDesc(
                LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime()), LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime()), pointOptions);

        if (rawData.isEmpty()) {
            return PointResponseDTO.builder().build();
        }

        // 조건에 따른 필터
        rawData = Optional.of(filterData(rawData.get(), storeList, salesOption));

        List<CategoryPointData> genderData = new ArrayList<>();
        List<CategoryPointData> ageData = new ArrayList<>();
        List<CategoryPointData> genreData = new ArrayList<>();
        List<CategoryPointData> storeData = new ArrayList<>();

        for (PointDealEntity pointDealEntity : rawData.get()) {

            // 성별 포인트 사용 내역
            String genderCategory = pointDealEntity.getUserContentEntity().getUserEntity().getUserGender().equals("male") ? "남성" : "여성";
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

        // 중복된 카테고리들은 합쳐서 반환 및 대여료와 연체료 부호 +로 변경
        genderData = changeSign(mergeCategoryData(genderData));
        ageData = changeSign(mergeCategoryData(ageData)).stream().sorted(Comparator.comparing(CategoryPointData::getCategory)).toList();
        genreData = changeSign(mergeCategoryData(genreData));
        storeData = changeSign(mergeCategoryData(storeData));



        return PointResponseDTO.builder()
                .gender(genderData)
                .age(ageData)
                .genre(genreData)
                .store(storeData)
                .build();

    }

    private List<CategoryPointData> changeSign(List<CategoryPointData> data) {
        return data.stream().map(categoryData -> {
            categoryData.setRentalFee(categoryData.getRentalFee() * -1);
            categoryData.setOverdueFee(categoryData.getOverdueFee() * -1);
            return categoryData;
        }).toList();
    }

    private List<CategoryPointData> mergeCategoryData(List<CategoryPointData> data) {
        Map<String, CategoryPointData> mergedData = new HashMap<>();
        for (CategoryPointData categoryData : data) {
            mergedData.computeIfAbsent(categoryData.getCategory(), k -> new CategoryPointData(k, 0L, 0L));
            CategoryPointData merged = mergedData.get(categoryData.getCategory());
            merged.setRentalFee(merged.getRentalFee() + categoryData.getRentalFee());
            merged.setOverdueFee(merged.getOverdueFee() + categoryData.getOverdueFee());
        }
        return new ArrayList<>(mergedData.values());
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

    public CategoryPointData getCategoryData(String category, PointDealEntity pointDealEntity) {

        return CategoryPointData.builder()
                .category(category)
                .rentalFee(pointDealEntity.getPointPayName().equals("대여료") ? pointDealEntity.getPointPrice() : 0L)
                .overdueFee(pointDealEntity.getPointPayName().equals("연체료") ? pointDealEntity.getPointPrice() : 0L)
                .build();
    }

    private List<PointDealEntity> filterData(List<PointDealEntity> data, List<String> storeList, String salesOption) {
        return data.stream()
                .filter(pointDeal -> storeList.isEmpty() || storeList.contains(pointDeal.getRent().getStoreCode().getStoreName().split(" ", 2)[1]))
                .filter(pointDeal -> salesOption.equals("전체") || pointDeal.getPointPayName().equals(salesOption))
                .toList();
    }

    public ResponseDTO getRevenueStatistics(String period) {

        LocalDate startDate = now;
        LocalDate endDate = LocalDate.now();

        startDate = getStartDate(period, startDate);

        Optional<List<PaymentEntity>> rawData = paymentRepository.findAllByReqDateBetween(
                LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime()), LocalDateTime.of(endDate, LocalDateTime.MAX.toLocalTime()));

        if (rawData.isEmpty()) {
            return ResponseDTO.builder().build();
        }

        List<CategoryData> genderData = new ArrayList<>();
        List<CategoryData> ageData = new ArrayList<>();

        for (PaymentEntity paymentEntity : rawData.get()) {

            // 성별 매출 내역
            String genderCategory = paymentEntity.getUserContentEntity().getUserEntity().getUserGender().equals("male") ? "남성" : "여성";
            genderData.add(new CategoryData(genderCategory, paymentEntity.getPaymentPrice()));

            // 나이별 매출 내역
            String birthday = paymentEntity.getUserContentEntity().getUserEntity().getUserBirthday();
            int ageNum = now.getYear() - Integer.parseInt(birthday.substring(0, 4)) + 1;

            String ageCategory = getAgeCategory(ageNum);

            ageData.add(new CategoryData(ageCategory, paymentEntity.getPaymentPrice()));
        }

        // 중복된 카테고리들은 합쳐서 반환
        genderData = mergeCategoryRevenueData(genderData);
        ageData = mergeCategoryRevenueData(ageData).stream().sorted(Comparator.comparing(CategoryData::getCategory)).toList();

        return ResponseDTO.builder()
                .gender(genderData)
                .age(ageData).build();
    }

    private List<CategoryData> mergeCategoryRevenueData(List<CategoryData> data) {
        Map<String, CategoryData> mergedData = new HashMap<>();
        for (CategoryData categoryRevenue: data) {
            mergedData.computeIfAbsent(categoryRevenue.getCategory(), k -> new CategoryData(k, 0L));
            CategoryData merged = mergedData.get(categoryRevenue.getCategory());
            merged.setCount(merged.getCount() + categoryRevenue.getCount());
        }
        return new ArrayList<>(mergedData.values());
    }

    private List<CategoryData> mergeCategoryCountData(List<CategoryData> data) {
        Map<String, CategoryData> mergedData = new HashMap<>();
        for (CategoryData categoryRevenue: data) {
            mergedData.computeIfAbsent(categoryRevenue.getCategory(), k -> new CategoryData(k, 0L));
            CategoryData merged = mergedData.get(categoryRevenue.getCategory());
            merged.setCount(merged.getCount() + 1);
        }
        return new ArrayList<>(mergedData.values());
    }

    public ResponseDTO getCustomerStatistics(String period) {

        LocalDate startDate = now;
        LocalDate endDate = LocalDate.now();

        startDate = getStartDate(period, startDate);

        Optional<List<UserEntity>> rawData = userRepository.findAllByCreateDateBetween(
                Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endDate.atStartOfDay().minusNanos(1)));

        if (rawData.isEmpty()) {
            return ResponseDTO.builder().build();
        }

        List<CategoryData> genderData = new ArrayList<>();
        List<CategoryData> ageData = new ArrayList<>();

        for (UserEntity userEntity : rawData.get()) {

            // 성별 회원 가입 내역
            String genderCategory = userEntity.getUserGender().equals("male") ? "남성" : "여성";
            genderData.add(new CategoryData(genderCategory, 1L));

            // 나이별 회원 가입 내역
            String birthday = userEntity.getUserBirthday();
            int ageNum = now.getYear() - Integer.parseInt(birthday.substring(0, 4)) + 1;

            String ageCategory = getAgeCategory(ageNum);

            ageData.add(new CategoryData(ageCategory, 1L));
        }

        // 중복된 카테고리들은 합쳐서 반환
        genderData = mergeCategoryRevenueData(genderData);
        ageData = mergeCategoryRevenueData(ageData).stream().sorted(Comparator.comparing(CategoryData::getCategory)).toList();

        return ResponseDTO.builder()
                .gender(genderData)
                .age(ageData).build();
    }

    public ResponseDTO getDelayStatistics(String period, List<String> storeList) {

        LocalDate startDate = now;
        LocalDate endDate = LocalDate.now();

        startDate = getStartDate(period, startDate);

        Optional<List<Overdue>> rawData = overdueRepository.findAllByOverStartBetween(
                startDate, endDate);

        if (rawData.isEmpty()) {
            return ResponseDTO.builder().build();
        }

        // 조건에 따른 필터
        rawData = Optional.of(filterDelayData(rawData.get(), storeList));

        List<CategoryData> genderData = new ArrayList<>();
        List<CategoryData> ageData = new ArrayList<>();
        List<CategoryData> genreData = new ArrayList<>();
        List<CategoryData> storeData = new ArrayList<>();

        for (Overdue overdue : rawData.get()) {

            // 성별 연체 내역
            String genderCategory = overdue.getRent().getUser().getUserGender().equals("male") ? "남성" : "여성";
            genderData.add(new CategoryData(genderCategory, overdue.getOverPrice()));

            // 나이별 연체 내역
            String birthday = overdue.getRent().getUser().getUserBirthday();
            int ageNum = now.getYear() - Integer.parseInt(birthday.substring(0, 4)) + 1;

            String ageCategory = getAgeCategory(ageNum);

            ageData.add(new CategoryData(ageCategory, overdue.getOverPrice()));

            // 장르별 연체 내역
            String genre = overdue.getRent().getBook().getBookInfoEntity().getGenre().getGenreName();

            genreData.add(new CategoryData(genre, overdue.getOverPrice()));

            // 지점별 연체 내역
            String store = overdue.getRent().getStoreCode().getStoreName().split(" ", 2)[1];

            storeData.add(new CategoryData(store, overdue.getOverPrice()));

        }

        // 중복된 카테고리들은 합쳐서 반환

        genderData = mergeCategoryCountData(genderData);
        ageData = mergeCategoryCountData(ageData).stream().sorted(Comparator.comparing(CategoryData::getCategory)).toList();
        genreData = mergeCategoryCountData(genreData);
        storeData = mergeCategoryCountData(storeData);

        return ResponseDTO.builder()
                .gender(genderData)
                .age(ageData)
                .genre(genreData)
                .store(storeData).build();
    }

    private List<Overdue> filterDelayData(List<Overdue> overdues, List<String> storeList) {
        return overdues.stream()
                .filter(overdue -> storeList.isEmpty() || storeList.contains(overdue.getRent().getStoreCode().getStoreName().split(" ", 2)[1]))
                .toList();
    }

    // stores 드롭다운에 표기될 지점명
    public List<String> getStores() {
        List<StoreEntity> storelist = storeRepository.findAllStoreNameByStoreStatus("open");
        return storelist.stream().map(StoreEntity::getStoreName).map(name -> {
            return name.split(" ", 2)[1];
        }).sorted().toList();
    }

    // genre 드롭다운에 표기될 장르명
    public List<String> getGenres() {
        List<GenreEntity> genreList = genreRepository.findAll();
        return genreList.stream().map(GenreEntity::getGenreName).toList();
    }
}
