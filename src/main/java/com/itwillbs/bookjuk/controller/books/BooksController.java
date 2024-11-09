package com.itwillbs.bookjuk.controller.books;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.service.books.BooksService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class BooksController {

    private final BooksService booksService;

    // 도서 리스트 불러오기 (HTML로 반환)
    @GetMapping("/admin/books")
    public String books(Model model) {
        List<BooksEntity> booksList = booksService.getAllBooks();  // BooksService에서 모든 도서 목록을 가져옴
        model.addAttribute("booksList", booksList);
        return "books/books";  // 도서 목록을 books/books.html로 전달
    }

    // 도서 등록 처리
    @PostMapping("/admin/addBook")
    public String addBookPost(@RequestBody BookDTO bookDTO) {
        log.info("BooksController addBookPost() - Books Entity: {}",bookDTO);
       BooksEntity booksEntity = new BooksEntity();
       BookInfoEntity bookInfoEntity = new BookInfoEntity();
       bookDTO.
       
        
        booksService.insertBooks(bookDTO);  // 도서 등록 서비스 호출
        return "redirect:/admin/books";  // 도서 등록 후 목록 페이지로 리다이렉트
    }

    // 책 목록을 JSON 형식으로 반환 (API 용)
    @GetMapping("/api/bookList")
    public List<BooksEntity> getBookList() {
        return booksService.getAllBooks();  // 책 목록을 JSON 형식으로 반환
    }
}
