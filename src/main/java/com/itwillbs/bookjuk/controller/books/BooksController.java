package com.itwillbs.bookjuk.controller.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.bookjuk.domain.books.BookStatus;
import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.repository.GenreRepository;
import com.itwillbs.bookjuk.repository.StoreRepository;
import com.itwillbs.bookjuk.service.books.BooksService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class BooksController {

	private final BooksService booksService;


	@GetMapping("/books")
	public String books(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "15") int size,
			@RequestParam(value = "search", defaultValue = "") String search) {
		log.info("Loading books with page={}, size={}, search={}", page, size, search);

		// 유효성 검사
		page = Math.max(page, 0); // 음수 방지
		size = Math.max(size, 1); // 최소 1개 페이지 크기 보장

		// Pageable 객체 생성
		Pageable pageable = PageRequest.of(page, size);

		// 데이터 조회: 검색어가 있을 경우 필터링
		Page<BookDTO> bookList = booksService.findByBookContaining(pageable, search);

		// 페이지네이션 계산
		int pageBlock = 10; // 페이지 블록 크기
		int startPage = (page / pageBlock) * pageBlock;
		int endPage = Math.min(startPage + pageBlock - 1, bookList.getTotalPages() - 1);

		// 지점 리스트 가져오기
		List<StoreEntity> storeList = booksService.getStoreList();

		// 장르ID 가져오기
		List<GenreEntity> genreList = booksService.getGenreList();
		// Thymeleaf로 데이터 전달
		model.addAttribute("bookList", bookList); // 도서 데이터
		model.addAttribute("currentPage", page); // 현재 페이지
		model.addAttribute("pageSize", size); // 페이지 크기
		model.addAttribute("totalPages", bookList.getTotalPages()); // 총 페이지 수
		model.addAttribute("startPage", startPage); // 시작 페이지
		model.addAttribute("endPage", endPage); // 끝 페이지
		model.addAttribute("search", search); // 검색어

		// 도서등록시 지점, 장르 선택 List 가져오기
		model.addAttribute("storeList", storeList);
		model.addAttribute("genreList", genreList);

		return "books/books"; // Thymeleaf 템플릿 이름
	}

	@PostMapping("/addBook")
	@ResponseBody
	public int addBook(@RequestBody BookDTO bookDTO) {
		log.info("Received bookDTO: genreId={}", bookDTO.getGenreId());
		booksService.insertBooks(bookDTO);
		return 1;
	}
	
	
}
