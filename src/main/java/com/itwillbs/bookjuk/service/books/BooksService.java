package com.itwillbs.bookjuk.service.books;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.GenreRepository;
import com.itwillbs.bookjuk.repository.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BooksService {

	// BooksRepository 객체생성
	private final BooksRepository booksRepository;

	// bookInfoRepository 객체생성
	private final BookInfoRepository bookInfoRepository;
	
	// storeRepository 객체생성
	private final StoreRepository storeRepository;
	
	// genreRepository 객체생성
	private final GenreRepository genreRepository;

	public Page<BooksEntity> getBookList(Pageable pageable) {
		log.info("BooksService getBooksList()");

		return booksRepository.findAll(pageable);
	}

	//도서 등록하기
	public void insertBooks(BooksEntity booksEntity, 
							BookInfoEntity bookInfoEntity) {
		//입고일
		//booksEntity.setBookDate(new Timestamp(System.currentTimeMillis()));
		
		booksRepository.save(booksEntity);
		bookInfoRepository.save(bookInfoEntity);
	}

	// 지점 List 조회
	public List<StoreEntity> getStroeList() {
		
		// 모든 지점 조회
		return storeRepository.findAll();
	
	}

	// 장르ID 조회
	public List<GenreEntity> getGenreList() {
		
		//모든 장르ID 조회
		return genreRepository.findAll();
	}

}
