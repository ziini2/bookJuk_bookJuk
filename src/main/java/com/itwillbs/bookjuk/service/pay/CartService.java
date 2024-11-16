package com.itwillbs.bookjuk.service.pay;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private BookInfoRepository bookInfoRepository;  // BookRepository 주입
    private BooksRepository booksRepository;

    private Map<Long, Integer> items;  // 장바구니 아이템 관리

    public CartService() {
        this.items = new HashMap<>();  // 장바구니 초기화
    }

    // 장바구니 아이템 반환
    public Map<Long, Integer> getItems() {
        return items;
    }

 // 장바구니에 상품 추가
    public void addItem(Long bookNum, int quantity) {
        // 도서 정보(DB에서 가져오기)
        Optional<BookInfoEntity> bookInfoOpt = bookInfoRepository.findById(bookNum);

    }

    // 장바구니에서 상품 제거
    public void removeItem(Long bookNum) {
        items.remove(bookNum);
    }

    // 장바구니 비우기
    public void clear() {
        items.clear();
    }
}
