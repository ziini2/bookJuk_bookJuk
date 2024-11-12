package com.itwillbs.bookjuk.service.rent;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;

    public List<RentEntity> findAll() {
        return rentRepository.findAll();
    }
}