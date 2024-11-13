package com.itwillbs.bookjuk.service.userPage;

import com.itwillbs.bookjuk.dto.UserPageBooksDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPageService {

    private final BooksRepository booksRepository;
    private final UserContentRepository userContentRepository;


    //대여가능한 책 리스트 반환
    public List<UserPageBooksDTO> getBooksList(int page, int pageSize) {
        //pageable 객체 생성
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BooksEntity> booksEntityList = booksRepository.findAllByRentStatusTrue(pageable);
        return convertBooksListToBooksDTOList(booksEntityList);
    }


    //userPageBooksDTO 리스트로 변환 메서드
    public List<UserPageBooksDTO> convertBooksListToBooksDTOList(Page<BooksEntity> booksEntityList) {
        return booksEntityList.stream().map(book -> UserPageBooksDTO.builder()
                .booksId(book.getBooksId())
                .bookNum(book.getBookInfoEntity())
                .storeCode(book.getStoreEntity().getStoreName())
                .bookStatus(book.getBookStatus())
                .rentStatus(book.getRentStatus())
                .build()).collect(Collectors.toList());
    }

    //userNum 값으로 userContentEntity 가져와서 포인트만 반환
    public int getUserPoint(Long userNum) {
        UserContentEntity userContentEntity = userContentRepository.findById(userNum).orElse(null);
        if (userContentEntity != null){
            return userContentEntity.getUserPoint();
        }
        return 0;
    }
}
