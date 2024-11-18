package com.itwillbs.bookjuk.service.books;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.repository.BooksRepository;
import com.itwillbs.bookjuk.repository.GenreRepository;
import com.itwillbs.bookjuk.repository.StoreRepository;
import com.itwillbs.bookjuk.util.BookConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final BookInfoRepository bookInfoRepository;
    private final StoreRepository storeRepository;
    private final GenreRepository genreRepository;

    // 도서 추가
    public void insertBooks(BookDTO bookDTO) {
        StoreEntity storeEntity = storeRepository.findById(bookDTO.getStoreCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid storeCode: " + bookDTO.getStoreCode()));

        GenreEntity genreEntity = genreRepository.findById(bookDTO.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid genreId: " + bookDTO.getGenreId()));

        BookInfoEntity bookInfoEntity = BookConverter.convertToBookInfoEntity(bookDTO, genreEntity);
        BookInfoEntity savedBookInfo = bookInfoRepository.save(bookInfoEntity);

        BooksEntity booksEntity = BookConverter.convertToBooksEntity(bookDTO, savedBookInfo, storeEntity);
        booksRepository.save(booksEntity);

        log.info("Books and BookInfo saved successfully");
    }

    // 필터를 통한 도서 검색
    public Page<BookDTO> findBooksByFilters(Pageable pageable, String search, Boolean rentalStatus, Long storeCode) {
        return booksRepository.findBooksByFilters(search, rentalStatus, storeCode, pageable)
                .map(BookConverter::convertToBookDTO);
    }

    // 모든 지점 목록 가져오기
    public List<StoreEntity> getStoreList() {
        return storeRepository.findAll();
    }

    // 모든 장르 목록 가져오기
    public List<GenreEntity> getGenreList() {
        return genreRepository.findAll();
    }

    // 도서 상세 조회
    public BookDTO getBookDetails(Long bookId) {
        log.info("Fetching book details for bookId: {}", bookId);

        BooksEntity bookEntity = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        return BookConverter.convertToBookDTO(bookEntity);
    }

    // 도서 수정
    public void updateBook(BookDTO bookDTO) {
        BooksEntity bookEntity = booksRepository.findById(bookDTO.getBooksId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookDTO.getBooksId()));

        BookInfoEntity bookInfoEntity = bookEntity.getBookInfoEntity();
        bookInfoEntity.setBookName(bookDTO.getBookName());
        bookInfoEntity.setAuthor(bookDTO.getAuthor());
        bookInfoEntity.setPublish(bookDTO.getPublish());
        bookInfoEntity.setPublishDate(bookDTO.getPublishDate());
        bookInfoEntity.setRentMoney(bookDTO.getRentMoney());

        
        bookEntity.setBookStatus(bookDTO.getBookStatus());
       
        // 저장
        bookInfoRepository.save(bookInfoEntity);
        booksRepository.save(bookEntity);
    }

}
