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

        if (bookInfoOpt.isPresent()) {
            BookInfoEntity bookInfo = bookInfoOpt.get();  // BookInfoEntity 가져오기

            // bookInfoEntity를 통해 해당 책의 재고 정보를 가진 BooksEntity 찾기
            List<BooksEntity> booksList = booksRepository.findByBookInfoEntity(bookInfo);  // 해당 bookInfo로 BooksEntity 조회

            if (!booksList.isEmpty()) {
                // 예시: 첫 번째 책을 가져와서 재고 확인 (이 부분은 조건에 맞게 개선 가능)
                BooksEntity booksEntity = booksList.get(0);  // 첫 번째 BooksEntity 가져오기 (여러 개일 수 있음)

                // 재고 확인
                if (booksEntity.getInventory() >= quantity) {
                    // 재고가 충분하면 장바구니에 상품 추가
                    // items.put(bookNum, items.getOrDefault(bookNum, 0) + quantity);
                    items.put(bookNum, items.getOrDefault(bookNum, 0) + quantity);  // 장바구니에 해당 도서 수량 추가
                } else {
                    throw new IllegalArgumentException("재고가 부족합니다.");
                }
            } else {
                throw new IllegalArgumentException("해당 도서에 대한 재고 정보가 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("도서 정보를 찾을 수 없습니다.");
        }
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
