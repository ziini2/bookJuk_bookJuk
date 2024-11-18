package com.itwillbs.bookjuk.service.books;

import java.util.List;
import java.util.Optional;

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

    public Page<BookDTO> findBooksByFilters(Pageable pageable, String search, Boolean rentalStatus, Long storeCode) {
        return booksRepository.findBooksByFilters(search, rentalStatus, storeCode, pageable)
                .map(BookConverter::convertToBookDTO);
    }

    public List<StoreEntity> getStoreList() {
        return storeRepository.findAll();
    }

    public List<GenreEntity> getGenreList() {
        return genreRepository.findAll();
    }
}
