package com.itwillbs.bookjuk.service.books;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.dto.BookDTO;
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
	public void insertBooks(BookDTO bookDTO) {
		
		// StoreEntity 객체
		StoreEntity storeEntity = storeRepository.findById(bookDTO.getStoreCode())
		    .orElseThrow(() -> new IllegalArgumentException("Invalid storeCode"));
		
		// GenreEntity 객체를 bookDTO의 genreId로 조회
	    GenreEntity genreEntity = genreRepository.findById(bookDTO.getGenreId())
	        .orElseThrow(() -> new IllegalArgumentException("Invalid genreId"));
		
		
		// BookInfoEntity 생성 먼저하고 저장 => bookNum 을 얻는다.
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
				.bookName(bookDTO.getBookName())
				.author(bookDTO.getAuthor())
				.publish(bookDTO.getPublish())
				.story(bookDTO.getStory())
				.genre(genreEntity) //장르연결 
				.isbn(bookDTO.getIsbn())
				.publishDate(bookDTO.getPublishDate())
				.rentMoney(bookDTO.getRentMoney())
				.build();
		log.info("bookInfoEntity : {}", bookInfoEntity.toString());
		
		//BookInfoEntity 먼저 저장하고 bookNum 생성
		BookInfoEntity savedBookInfo = bookInfoRepository.save(bookInfoEntity);
	    log.info("bookInfoEntity : {}", savedBookInfo.toString());
	    
	    
		//BooksEntity 에 저장된 bookInfoEntity 사용
		BooksEntity booksEntity = BooksEntity.builder()
	        	    .bookStatus(bookDTO.getBookStatus())
	        	    .rentStatus(true)
	        	    .storeEntity(storeEntity)
	        	    .bookInfoEntity(savedBookInfo) // 저장된 bookInfoEntity 사용함
	        	    .build();


	    log.info("booksEntity : {}", booksEntity.toString());
	    log.info("savedBookInfo : {}", savedBookInfo.toString());
	    
	    // BooksEntity 저장
		booksRepository.save(booksEntity);
		
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
