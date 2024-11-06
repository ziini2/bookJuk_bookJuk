package com.itwillbs.bookjuk.controller.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.service.books.BooksService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class BooksController {
	// 객체생성
	private final BooksService booksService;

	// 도서 등록
	@GetMapping("/admin/addBook")
	public String addBook() {
		log.info("BooksController addBook()");
		return "/books/addBook";
	}

	@PostMapping("/admin/addBook")
	public String addBookPost(BookInfoEntity bookInfoEntity, BooksEntity booksEntity) {
		log.info("BooksController addBookPost()");
		log.info(bookInfoEntity.toString());
		log.info(booksEntity.toString());
		
		//booksService.insertBooks(booksEntity, bookInfoEntity);

		return "redirect:/admin/books";
		
	}

	// 도서목록
	@GetMapping("/admin/books")
	public String books(Model model, @RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "size", defaultValue = "5", required = false) int size) {
		log.info("BooksController books()");

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("bookNum").descending());
		Page<BooksEntity> bookList = booksService.getBookList(pageable);

		model.addAttribute("bookList", bookList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);

		// 전체 페이지 개수
		model.addAttribute("totalPages", bookList.getTotalPages());

		// 한화면에 보여줄 페이지 개수
		int pageBlock = 5;
		int startPage = (page - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if (endPage > bookList.getTotalPages()) {
			endPage = bookList.getTotalPages();
		}

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		return "/books/books";
	}
}
