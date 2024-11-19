package com.itwillbs.bookjuk.service.pay;

import com.itwillbs.bookjuk.domain.pay.PointPayStatus;
import com.itwillbs.bookjuk.entity.UserContentEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import com.itwillbs.bookjuk.entity.rent.RentEntity;
import com.itwillbs.bookjuk.repository.*;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
	private final BooksRepository booksRepository;
	private final RentRepository rentRepository;
	private final UserRepository userRepository;
	private final UserContentRepository userContentRepository;
	private final PointDealRepository pointDealRepository;

	public List<BooksEntity> getUserCartItems(List<Long> bookList) {
		// 세션에서 장바구니 리스트 가져오기
		// 장바구니에 담긴 Books 데이터를 조회하고 BookInfo 데이터를 반환
		log.info("bookList: {}", bookList);
		return booksRepository.findByBooksIdList(bookList);
	}

	public Map<String, Integer> getMyCartBookInfo(List<BooksEntity> bookList) {
		  return Map.of(
				  "totalBookCount", bookList.size(),
				  "totalPrice", getTotalBookPrice(bookList)
		  );
	}

	//대여 금액 총합
	public int getTotalBookPrice(List<BooksEntity> bookList) {
		return bookList.stream()
				.mapToInt(booksEntity -> booksEntity.getBookInfoEntity().getRentMoney().intValue()) // Long 값을 int로 변환
				.sum();
	}

	public boolean myCartBooksRent(List<Long> myCartBookId, Long myPoint) {
		List<BooksEntity> myCartBooks = booksRepository.findByBooksIdList(myCartBookId);
		//1.총 금액과 , 나의포인트 비교 해서 내 포인트가 총금액과 같거나 많아야함. (userContent table)
		int totalBookPrice = getTotalBookPrice(myCartBooks);
		if (myPoint != null && totalBookPrice <= myPoint ){
			//2.포인트를 사용하고 포인트 거래 내역에 추가 한다 (pointDeal table)
			UserContentEntity user = userContentRepository.findByUserEntity_UserNum(SecurityUtil.getUserNum());
			user.setBringBook(user.getBringBook() + myCartBookId.size());
			user.setUserPoint(user.getUserPoint() - totalBookPrice);
			userContentRepository.save(user);

			//3.책이 어떤 책인지 판단해서 대여목록에 추가한다 (rent table)
			for (Long aLong : myCartBookId) {
				BooksEntity newBooksEntity = booksRepository.findByBooksId(aLong);
				newBooksEntity.getBookInfoEntity().setRentCount(newBooksEntity.getBookInfoEntity().getRentCount() + 1);
				newBooksEntity.setRentStatus(false);
				booksRepository.save(newBooksEntity);

				RentEntity newRent = RentEntity.builder()
						.user(user.getUserEntity())
						.rentStart(LocalDate.now())
						.rentEnd(LocalDate.now().plusDays(6))
						.rentPrice(Integer.parseInt(String.valueOf(newBooksEntity.getBookInfoEntity().getRentMoney())))
						.storeCode(newBooksEntity.getStoreEntity())
						.rentStatus(false)
						.book(booksRepository.findByBooksId(aLong))
						.build();
				rentRepository.save(newRent);

				PointDealEntity newPointDeal = PointDealEntity.builder()
						.pointPrice(Integer.parseInt(String.valueOf(newBooksEntity.getBookInfoEntity().getRentMoney())) * -1)
						.pointPayStatus(PointPayStatus.SUCCESSFUL)
						.pointPayName("대여료")
						.userContentEntity(user)
						.rent(newRent)
						.build();
				pointDealRepository.save(newPointDeal);
			}
			return true;
		}
		return false;
	}
}
    

