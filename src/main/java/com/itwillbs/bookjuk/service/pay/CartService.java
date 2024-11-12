//package com.itwillbs.bookjuk.service.pay;
//
//import com.itwillbs.bookjuk.domain.Book;
//import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
//import com.itwillbs.bookjuk.entity.books.BooksEntity;
//import com.itwillbs.bookjuk.repository.BookInfoRepository;
//import com.itwillbs.bookjuk.repository.BookRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class CartService {
//
//    @Autowired
//    private BookInfoRepository bookInfoRepository;  // BookRepository 주입
//
//    private Map<Long, Integer> items;  // 장바구니 아이템 관리
//
//    public CartService() {
//        this.items = new HashMap<>();  // 장바구니 초기화
//    }
//
//    // 장바구니 아이템 반환
//    public Map<Long, Integer> getItems() {
//        return items;
//    }
//
//    // 장바구니에 상품 추가
//    public void addItem(Long productId, int quantity) {
//        // 도서 정보를 DB에서 조회
//        Optional<BookInfoEntity> bookOpt = bookInfoRepository.findById(productId);
//        
//        if (bookOpt.isPresent()) {
//            BookInfoEntity bookInfo = bookOpt.get();  // 도서 정보 객체 가져오기
//
//        // bookInfoEntity에 해당하는 모든 booksEntity 조회
//        List<BooksEntity> booksEntityInven = bookInfo.get;  // 여러 재고 항목
//
//            // 재고 확인
//            if (book.get>= quantity) {
//                // 재고가 충분하면 장바구니에 상품 추가
//                items.put(productId, items.getOrDefault(productId, 0) + quantity);
//            } else {
//                throw new IllegalArgumentException("재고가 부족합니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("도서를 찾을 수 없습니다.");
//        }
//    }
//
//    // 장바구니에서 상품 제거
//    public void removeItem(Long productId) {
//        items.remove(productId);
//    }
//
//    // 장바구니 비우기
//    public void clear() {
//        items.clear();
//    }
//}
