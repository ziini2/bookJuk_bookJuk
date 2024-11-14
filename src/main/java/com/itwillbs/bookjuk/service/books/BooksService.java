package com.itwillbs.bookjuk.service.books;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private final BooksRepository booksRepository;
    private final BookInfoRepository bookInfoRepository;
    private final StoreRepository storeRepository;
    private final GenreRepository genreRepository;

    public Page<BooksEntity> getBookList(Pageable pageable) {
        log.info("BooksService getBooksList() called");
        return booksRepository.findAll(pageable);
    }

    // 도서 등록
    public void insertBooks(BookDTO bookDTO) {
        // StoreEntity 찾기
        StoreEntity storeEntity = storeRepository.findById(bookDTO.getStoreCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid storeCode: " + bookDTO.getStoreCode()));

        // GenreEntity 찾기
        GenreEntity genreEntity = genreRepository.findById(bookDTO.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid genreId: " + bookDTO.getGenreId()));

        // BookInfoEntity 생성 및 저장
        BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
                .bookName(bookDTO.getBookName())
                .author(bookDTO.getAuthor())
                .publish(bookDTO.getPublish())
                .story(bookDTO.getStory())
                .genre(genreEntity)
                .isbn(bookDTO.getIsbn())
                .bookImage(bookDTO.getBookImage())
                .publishDate(bookDTO.getPublishDate())
                .rentMoney(bookDTO.getRentMoney() != null ? bookDTO.getRentMoney() : 0L) // rentMoney 기본값
                .rentCount(bookDTO.getRentCount() != null ? bookDTO.getRentCount() : 0L) // rentCount 기본값
                .build();

        log.info("BookInfoEntity before save: {}", bookInfoEntity);
        BookInfoEntity savedBookInfo = bookInfoRepository.save(bookInfoEntity);
        log.info("Saved BookInfoEntity: {}", savedBookInfo);

        // BooksEntity 생성 및 저장
        BooksEntity booksEntity = BooksEntity.builder()
                .bookStatus(bookDTO.getBookStatus() != null ? bookDTO.getBookStatus() : BookStatus.GOOD) // Enum 사용
                .rentStatus(true)
                .storeEntity(storeEntity)
                .bookInfoEntity(savedBookInfo)
                .build();


        log.info("BooksEntity before save: {}", booksEntity);
        booksRepository.save(booksEntity);
        log.info("Saved BooksEntity: {}", booksEntity);
    }

    // 지점 List 조회
    public List<StoreEntity> getStoreList() {
        return storeRepository.findAll();
    }

    // 장르ID 조회
    public List<GenreEntity> getGenreList() {
        return genreRepository.findAll();
    }

    public Page<BookDTO> findByBookContaining(Pageable pageable, String search) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Searching for books with keyword: {}", search);

        try {
            String searchKeyword = StringUtils.hasText(search) ? search.trim() : "";

            Page<BookInfoEntity> bookInfoPage = bookInfoRepository.findByBookNameContaining(searchKeyword, pageable);
            return bookInfoPage.map(this::convertToBookDTO);
        } catch (DataAccessException e) {
            logger.error("Error occurred while searching for books", e);
            throw new RuntimeException("데이터베이스 조회 중 오류가 발생했습니다.", e);
        }
    }

    private BookDTO convertToBookDTO(BookInfoEntity bookInfo) {
        return BookDTO.builder()
                .isbn(bookInfo.getIsbn())
                .bookImage(bookInfo.getBookImage())
                .bookName(bookInfo.getBookName())
                .author(bookInfo.getAuthor())
                .publish(bookInfo.getPublish())
                .publishDate(bookInfo.getPublishDate())
                .story(bookInfo.getStory())
                .genreId(bookInfo.getGenre().getGenreId())
                .bookDate(Timestamp.valueOf(bookInfo.getBookDate()))
                .rentMoney(bookInfo.getRentMoney())
                .rentCount(bookInfo.getRentCount())
                .storeCode(null)
                .bookStatus(null)
                .build();
    }
}
