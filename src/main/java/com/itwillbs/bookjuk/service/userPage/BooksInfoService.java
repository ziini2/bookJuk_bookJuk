package com.itwillbs.bookjuk.service.userPage;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.dto.UserPageBooksDTO;
import com.itwillbs.bookjuk.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BooksInfoService {

    private final BooksRepository booksRepository;

    //책 상세정보 불러오기
    public UserPageBooksDTO getBookInfo(Long booksId) {
        log.info("getBookInfo: booksId={}", booksId);
        return booksRepository.findById(booksId)
                .map(booksEntity -> UserPageBooksDTO.builder()
                        .booksId(booksEntity.getBooksId())
                        .bookNum(booksEntity.getBookInfoEntity())
                        .storeCode(booksEntity.getStoreEntity().getStoreName())
                        .build())
                .orElse(null);
    }


}
