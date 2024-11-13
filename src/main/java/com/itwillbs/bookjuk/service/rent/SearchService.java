package com.itwillbs.bookjuk.service.rent;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.rent.SearchResultDTO;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.SearchRepository;
import com.itwillbs.bookjuk.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class SearchService {
//
//	@Autowired
//    private SearchRepository searchRepository;
//
//	@Autowired
//    private BooksRepository booksRepository;
//
//    @Autowired
//    private BookInfoRepository bookInfoRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    public List<UserEntity> searchUsers(String criteria, String keyword) {
//        if ("userName".equals(criteria)) {
//            return searchRepository.findByUserNameContainingIgnoreCase(keyword);
//        } else if ("userId".equals(criteria)) {
//            return searchRepository.findByUserIdContainingIgnoreCase(keyword);
//        } else {
//            return List.of(); // 조건이 맞지 않으면 빈 리스트 반환
//        }
//    }
//
//    public List<SearchResultDTO> searchBooks(String criteria, String keyword) {
//        List<BooksEntity> books;
//
//        if ("bookName".equals(criteria)) {
//            // bookName을 기준으로 BooksEntity에서 검색
//            books = booksRepository.findByStoreCode(null);
//        } else if ("bookNum".equals(criteria)) {
//            try {
//                Long bookNum = Long.parseLong(keyword);
//                books = booksRepository.findByBookNum(bookNum);
//            } catch (NumberFormatException e) {
//                return List.of(); // 숫자가 아닌 경우 빈 리스트 반환
//            }
//        } else {
//            return List.of(); // 조건이 맞지 않으면 빈 리스트 반환
//        }
//
//        // BooksEntity와 BookInfoEntity 및 StoreEntity에서 필요한 정보를 가져와서 DTO로 변환
//        return books.stream()
//                .map(book -> {
//                    BookInfoEntity bookInfo = bookInfoRepository.findById(book.getBookNum()).orElse(null);
//                    StoreEntity store = storeRepository.findById(book.getStoreCode()).orElse(null);
//                    String bookName = (bookInfo != null) ? bookInfo.getBookName() : "책 정보 없음";
//                    String storeName = (store != null) ? store.getStoreName() : "지점 정보 없음";
//
//                    return new SearchResultDTO(
//                            book.getBookNum(),
//                            bookName,
//                            storeName
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//
//
//
}
