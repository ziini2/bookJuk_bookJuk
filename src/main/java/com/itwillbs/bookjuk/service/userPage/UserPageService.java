package com.itwillbs.bookjuk.service.userPage;

import com.itwillbs.bookjuk.dto.UserPageBooksDTO;
import com.itwillbs.bookjuk.dto.UserPaginationDTO;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.UserContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPageService {

    private final BooksRepository booksRepository;
    private final UserContentRepository userContentRepository;


    //대여가능한 책 리스트 반환
    public UserPaginationDTO getBooksList(int page, int pageSize) {
        //pageable 객체 생성
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BooksEntity> booksEntityList = booksRepository.findAllByRentStatusTrue(pageable);
        return convertBooksListToBooksDTOList(booksEntityList);
    }


    //UserPaginationDTO 로 변환 메서드
    public UserPaginationDTO convertBooksListToBooksDTOList(Page<BooksEntity> booksEntityList) {
        List<UserPageBooksDTO> userBookDTO = booksEntityList.stream().map(book -> UserPageBooksDTO.builder()
                .booksId(book.getBooksId())
                .bookNum(book.getBookInfoEntity())
                .storeCode(book.getStoreEntity().getStoreName())
                .bookStatus(book.getBookStatus())
                .rentStatus(book.getRentStatus())
                .build()).toList();

        return UserPaginationDTO.builder()
                .userPageBooksDTO(userBookDTO)
                .totalPages(booksEntityList.getTotalPages())
                .currentPage(booksEntityList.getNumber())
                .hasNext(booksEntityList.hasNext())
                .hasPrevious(booksEntityList.hasPrevious())
                .build();
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
