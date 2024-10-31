package com.itwillbs.bookjuk.service.books;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.BooksEntity;
import com.itwillbs.bookjuk.repository.BooksRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BooksService {
	
	// BooksRepository 객체생성
	private final BooksRepository booksRepository;
	
	
	public void insertBooks(BooksEntity booksEntity) {
		log.info("BooksService insertBooks()");
		
		//책 입고일
		booksEntity.setBookDate(new Timestamp(System.currentTimeMillis()));
		
		booksRepository.save(booksEntity);
	}


	public Page<BooksEntity> getBookList(Pageable pageable) {
		log.info("BooksService getBooksList()");
		
		return booksRepository.findAll(pageable);
	}
	
	

	
}

