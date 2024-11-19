package com.itwillbs.bookjuk.service.pay;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.RentRepository;
import com.itwillbs.bookjuk.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
public class CartService {

	  @Autowired
	  private final BooksRepository booksRepository;
	  private final RentRepository rentRepository;
	  private final UserRepository userRepository;

	    public CartService(BooksRepository booksRepository, RentRepository rentRepository, UserRepository userRepository) {
	        this.booksRepository = booksRepository;
	        this.rentRepository = rentRepository;
	        this.userRepository = userRepository;
	    }
	    
	    

	    public List<BookInfoEntity> getUserCartItems(HttpSession session) {
	        // 세션에서 장바구니 리스트 가져오기
	        ArrayList<Long> myCartBookIdList = (ArrayList<Long>) session.getAttribute("myCartBookId");
	        if (myCartBookIdList == null || myCartBookIdList.isEmpty()) {
	            return new ArrayList<>();
	        }

	        // 장바구니에 담긴 Books 데이터를 조회하고 BookInfo 데이터를 반환
	        List<BookInfoEntity> selectedBooks = new ArrayList<>();
	        for (Long booksId : myCartBookIdList) {
	            Optional<BooksEntity> booksEntity = booksRepository.findById(booksId);
	            booksEntity.ifPresent(books -> selectedBooks.add(books.getBookInfoEntity()));
	        }

	        return selectedBooks;
	    }
	    
	    public List<BookInfoEntity> getSelectedBooks(List<Long> selectedBookIds) {
	        List<BookInfoEntity> selectedBooks = new ArrayList<>();
	        for (Long bookId : selectedBookIds) {
	            Optional<BooksEntity> booksEntity = booksRepository.findById(bookId);
	            booksEntity.ifPresent(book -> selectedBooks.add(book.getBookInfoEntity()));
	        }
	        return selectedBooks;
	    }

	    //대여 금액 총합
	    public int calculateTotalAmount(List<BookInfoEntity> bookInfoEntity) {
	        int totalAmount = 0;
	        for (BookInfoEntity book : bookInfoEntity) {
	            totalAmount += book.getRentMoney();  // 각 책의 대여 금액을 더합니다.
	        }
	        return totalAmount;
	    }

//	    public boolean rentBooks(List<Long> bookIds, Long userNum) {
//	        // 로그인된 사용자 정보 조회
//	        UserEntity user = userRepository.findById(userNum)
//	                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//	        List<BooksEntity> books = booksRepository.findAllById(bookIds);
//
//	        for (BooksEntity book : books) {
//	            // 대여 불가능한 상태 체크
//	            if (!"FAN".equals(book.getRentStatus()) || !"GOOD".equals(book.getBookStatus())) {
//	                throw new RuntimeException("대여 불가능한 도서가 포함되어 있습니다.");
//	            }
//	        }
//
//	        for (BooksEntity book : books) {
//	            // RentEntity 생성 및 저장
//	            RentEntity rent = RentEntity.builder()
//	                    .user(user) // 연관된 사용자 엔티티 설정
//	                    .storeCode(book.getStoreCode()) // 책의 매장 정보 설정
//	                    .book(book) // 책 엔티티 설정
//	                    .rentStart(LocalDate.now())
//	                    .rentEnd(LocalDate.now().plusDays(7)) // 7일 대여
//	                    .rentPrice(book.getBookInfoEntity().getRentMoney())
//	                    .rentStatus(false) // 대여 상태
//	                    .build();
//	            rentRepository.save(rent);
//
//	            // 도서 상태 업데이트
//	            book.setRentStatus("ROOM"); // 대여 중 상태
//	            booksRepository.save(book);
//	        }
//
//	        return true;
//	    }
//	   
}
//    // 장바구니에 담긴 책 ID 리스트를 세션에서 가져오기
//    public List<Long> getCart(Long userNum, HttpSession session) {
//        // 세션에서 장바구니 리스트 가져오기
//        List<Long> myCartBookIdList = (List<Long>) session.getAttribute("myCartBookId");
//        
//        // 만약 장바구니에 아무런 책도 없으면 새로 생성
//        if (myCartBookIdList == null) {
//            myCartBookIdList = new ArrayList<>();
//        }
//        
//        return myCartBookIdList;
//    }
//
//    // 장바구니에 책 추가
//    public void addToCart(Long userNum, Long bookId, HttpSession session) {
//        // 세션에서 장바구니 리스트 가져오기
//        List<Long> myCartBookIdList = getCart(userNum, session);
//
//        // 중복 방지: 이미 장바구니에 추가된 책은 다시 추가하지 않음
//        if (!myCartBookIdList.contains(bookId)) {
//            myCartBookIdList.add(bookId);
//        }
//
//        // 세션에 업데이트된 장바구니 리스트 저장
//        session.setAttribute("myCartBookId", myCartBookIdList);
//    }
//
//    // 장바구니에서 책 삭제
//    public void removeFromCart(Long userNum, Long bookId, HttpSession session) {
//        // 세션에서 장바구니 리스트 가져오기
//        List<Long> myCartBookIdList = getCart(userNum, session);
//
//        // 장바구니에 책이 있다면 삭제
//        if (myCartBookIdList.contains(bookId)) {
//            myCartBookIdList.remove(bookId);
//        }
//
//        // 세션에 업데이트된 장바구니 리스트 저장
//        session.setAttribute("myCartBookId", myCartBookIdList);
//    }
//
//    // 장바구니에 담긴 책 ID 리스트를 바탕으로 책 정보 리스트 조회
//    public List<BookInfoEntity> getBooksFromIds(List<Long> bookIds) {
//        // 책 정보 조회 (BookInfoRepository는 데이터베이스에서 책 정보를 조회하는 역할)
//        if (bookIds == null || bookIds.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return bookInfoRepository.findAllById(bookIds);
//    }
//    
//    //대여 금액 총합
//    public int calculateTotalAmount(List<BookInfoEntity> bookInfoEntity) {
//        int totalAmount = 0;
//        for (BookInfoEntity book : bookInfoEntity) {
//            totalAmount += book.getRentMoney();  // 각 책의 대여 금액을 더합니다.
//        }
//        return totalAmount;
//    }
//    
//    // 대여 완료 후 포인트 거래 처리
//    @Transactional
//    public void completeRent(List<Long> selectedBookIds, Long userId, Long couponId) {
//        // 선택된 책들 대여 처리
//        List<BooksEntity> rentEntities = booksRepository.findBooksByIds(selectedBookIds);
//        
//        for (RentEntity rent : rentEntities) {
//            // 대여 처리 로직: 책을 대여 처리하는 로직 필요
//            rent.setStatus(RentStatus.RENTED);  // 대여 상태 업데이트
//            rent.setRentDate(LocalDateTime.now());
//            rentRepository.save(rent);  // 대여 정보 DB에 저장
//        }
//
//        // 유저 정보 가져오기
//        UserContentEntity userContent = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 포인트 계산 (예시: 각 대여마다 100P)
//        int totalPoints = rentEntities.size() * 100;  // 예시로 대여한 책 개수만큼 포인트 지급
//
//        // 포인트 거래 생성
//        PointDealEntity pointDeal = PointDealEntity.builder()
//                .pointPrice(totalPoints)  // 포인트 금액
//                .pointPayStatus(PointPayStatus.COMPLETED)  // 거래 상태 (대여 완료)
//                .reqDate(LocalDateTime.now())  // 요청 일시
//                .pointPayName("대여 포인트")  // 거래 품목명
//                .userContentEntity(userContent)  // 유저 정보
//                .couponId(couponRepository.findById(couponId).orElse(null))  // 쿠폰 정보 (선택적으로 사용)
//                .rent(rentEntities.get(0))  // 첫 번째 대여 정보 (하나의 대여로 처리)
//                .build();
//
//        // 포인트 거래 저장
//        pointDealRepository.save(pointDeal);
//    }
    

