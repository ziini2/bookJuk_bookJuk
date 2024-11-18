package com.itwillbs.bookjuk.controller.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.service.books.BooksService;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class BooksController {

	private final BooksService booksService;

	@GetMapping("/books")
	public String books(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "15") int size,
			@RequestParam(value = "search", defaultValue = "") String search,
			@RequestParam(value = "rentalStatus", required = false, defaultValue = "1") String rentalStatus,
			@RequestParam(value = "storeCode", required = false) Long storeCode) {

		log.info("Loading books with page={}, size={}, search={}, rentalStatus={}, storeCode={}", page, size, search,
				rentalStatus, storeCode);

		Pageable pageable = PageRequest.of(page, size);

		Boolean rentalStatusBoolean = null;
		if (rentalStatus != null) {
			rentalStatusBoolean = rentalStatus.equals("1");
		}

		Page<BookDTO> bookList = booksService.findBooksByFilters(pageable, search, rentalStatusBoolean, storeCode);

		int totalPages = bookList.getTotalPages();
		int pageBlock = 10; // 한 블록에 표시할 페이지 수
		int firstPageInBlock = (page / pageBlock) * pageBlock; // 블록의 첫 페이지
		int lastPageInBlock = Math.min(firstPageInBlock + pageBlock - 1, totalPages - 1); // 블록의 마지막 페이지

		model.addAttribute("bookList", bookList);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", firstPageInBlock); // startPage 전달
		model.addAttribute("endPage", lastPageInBlock); // endPage 전달
		model.addAttribute("search", search);
		model.addAttribute("rentalStatus", rentalStatus);
		model.addAttribute("storeCode", storeCode);
		model.addAttribute("storeList", booksService.getStoreList());
		model.addAttribute("genreList", booksService.getGenreList());

		return "books/books";
	}

	@PostMapping("/addBook")
	@ResponseBody
	public int addBook(@RequestBody BookDTO bookDTO) {
		log.info("Received bookDTO for adding a new book: {}", bookDTO);
		booksService.insertBooks(bookDTO);
		log.info("Book added successfully");
		return 1;
	}

	// 도서 상세 조회
	@GetMapping("/books/{bookId}")
	@ResponseBody
	public ResponseEntity<BookDTO> getBookDetails(@PathVariable Long bookId) {
		log.info("Fetching details for bookId: {}", bookId);
		BookDTO book = booksService.getBookDetails(bookId);
		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			log.warn("Book not found with id: {}", bookId);
			return ResponseEntity.notFound().build();
		}
	}

	// 도서 수정
	@PostMapping("/books/update")
	@ResponseBody
	public ResponseEntity<String> updateBook(@RequestBody BookDTO bookDTO) {
		log.info("Received request to update book: {}", bookDTO);
		try {
			booksService.updateBook(bookDTO); // 서비스 호출
			return ResponseEntity.ok("Book updated successfully");
		} catch (Exception e) {
			log.error("Error updating book: {}", e.getMessage());
			return ResponseEntity.status(500).body("Failed to update book");
		}
	}

}