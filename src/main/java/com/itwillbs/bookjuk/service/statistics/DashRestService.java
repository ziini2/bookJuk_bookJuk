package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.repository.PaymentRepository;
import com.itwillbs.bookjuk.repository.PointDealRepository;
import com.itwillbs.bookjuk.repository.RentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;
import com.itwillbs.bookjuk.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
}
