package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.itwillbs.bookjuk.repository.PointDealRepository;
import com.itwillbs.bookjuk.repository.RentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashRestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;
    private final PointDealRepository pointDealRepository;
    private final RentRepository rentRepository;
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

        return paymentRepository.sumAmountByReqDateBetween(startOfDay, endOfDay);

    }

    public Long getDailyPoint() {

        LocalDateTime startOfDay = now.atStartOfDay();
        LocalDateTime endOfDay = now.plusDays(1).atStartOfDay().minusNanos(1);

        return pointDealRepository.sumAmountByReqDateBetween(startOfDay, endOfDay);
    }

    public long getDailyRental() {

        Timestamp startOfDay = Timestamp.valueOf(now.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(now.plusDays(1).atStartOfDay().minusNanos(1));

        return rentRepository.countByRentDateBeforeAndReturnDateAfterAndReturnInfo(startOfDay, endOfDay, "대여중");
    }


    public long getDailyDelay() {

        Timestamp returnDay = Timestamp.valueOf(now.atStartOfDay());

        return rentRepository.countByReturnDateBeforeAndReturnInfo(returnDay, "대여중");
    }
}
