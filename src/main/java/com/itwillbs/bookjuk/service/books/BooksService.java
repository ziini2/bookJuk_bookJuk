package com.itwillbs.bookjuk.service.books;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BooksService {
	private final BooksRepository booksRepository;
	private final BookInfoRepository bookInfoRepository;

	@Transactional
	public void insertBooks(BooksEntity booksEntity, BookInfoEntity bookInfoEntity) {
		 // BookInfoEntity를 먼저 저장하여 bookNum 생성
	    BookInfoEntity savedBookInfo = bookInfoRepository.save(bookInfoEntity);
	    booksEntity.setBookNum(savedBookInfo.getBookNum());
	    booksRepository.save(booksEntity);
	}
	
	// 도서 목록을 가져오는 메서드
	public List<BooksEntity> getAllBooks() {
		return booksRepository.findAll(); // BooksRepository에서 제공하는 findAll 메서드 사용
	}
}
