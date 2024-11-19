package com.itwillbs.bookjuk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BooksRepository extends JpaRepository<BooksEntity, Long> {

	// 대여등록 회원검색
	List<BooksEntity> findByBookInfoEntity_BookNum(Long bookNum);

	// 지점코드
	List<BooksEntity> findByStoreEntity_StoreCode(Long storeCode);

	// 장바구니
	@Query("SELECT b FROM BooksEntity b WHERE b.booksId IN :bookNums")
	List<BooksEntity> findByBooksIdList(@Param("bookNums") List<Long> bookNums);

	// 전체 대여가능 목록 데이터 가져오기
	@Query("SELECT b " + "FROM BooksEntity b JOIN b.bookInfoEntity bi JOIN bi.genre g JOIN b.storeEntity s "
			+ "WHERE b.rentStatus = true "
			+ "AND (:keyword IS NULL OR bi.bookName LIKE %:keyword% OR bi.author LIKE %:keyword%) "
			+ "AND (:genre IS NULL OR g.genreName = :genre) " + "AND (:store IS NULL OR s.storeName = :store) ")
	Page<BooksEntity> findAllByRentStatusTrue(@Param("keyword") String keyword, @Param("genre") String genre,
			@Param("store") String store, Pageable pageable);

	Optional<List<BooksEntity>> findAllByBookInfoEntityIn(List<BookInfoEntity> bookNums);

	@Query("SELECT b FROM BooksEntity b " + "JOIN b.bookInfoEntity bi "
			+ "WHERE (:search IS NULL OR bi.bookName LIKE %:search%) "
			+ "AND (:rentalStatus IS NULL OR b.rentStatus = :rentalStatus) "
			+ "AND (:storeCode IS NULL OR b.storeEntity.storeCode = :storeCode)")
	Page<BooksEntity> findBooksByFilters(@Param("search") String search, @Param("rentalStatus") Boolean rentalStatus,
			@Param("storeCode") Long storeCode, Pageable pageable);

	BooksEntity findByBooksId(Long booksID);

}
