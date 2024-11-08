package com.itwillbs.bookjuk.service.statistics;

import com.itwillbs.bookjuk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DashRestServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashRestService dashRestService;

    public DashRestServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNumbersOfNewCustomers() {
        // Arrange: 날짜 및 예상 결과 준비
        LocalDate now = LocalDate.now();
        Timestamp startOfDay = Timestamp.valueOf(now.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(now.plusDays(1).atStartOfDay().minusNanos(1));

        // 예상하는 고객 수
        long expectedNewCustomers = 1L;

        // Mock: UserRepository의 countByCreateDateBetween이 예상 결과를 반환하도록 설정
        when(userRepository.countByCreateDateBetween(startOfDay, endOfDay)).thenReturn(expectedNewCustomers);

        // Act: 메서드 실행
        long actualNewCustomers = dashRestService.getNumbersOfNewCustomers();

        // Assert: 결과 확인
        assertEquals(expectedNewCustomers, actualNewCustomers);

        // Verify: 지정된 날짜 범위로 countByCreateDateBetween이 호출되었는지 검증
        verify(userRepository, times(1)).countByCreateDateBetween(startOfDay, endOfDay);
    }
}
