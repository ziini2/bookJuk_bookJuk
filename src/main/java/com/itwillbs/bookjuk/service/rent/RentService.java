package com.itwillbs.bookjuk.service.rent;

import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;

    public Page<RentEntity> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rentNum").descending());
        Page<RentEntity> rentEntities = rentRepository.findAll(pageable);
        log.debug("Loaded Rent Entities: {}", rentEntities.getContent());
        return rentEntities;
    }
}
