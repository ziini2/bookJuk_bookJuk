package com.itwillbs.bookjuk.controller.books;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.service.books.BooksService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class BooksController {

	private final BooksService booksService;

	@GetMapping("/books")
	public String books(Model model) {

		// 지점 리스트 가져오기
		List<StoreEntity> storeList = booksService.getStroeList();

		// 장르ID 가져오기
		List<GenreEntity> genreList = booksService.getGenreList();

		model.addAttribute("storeList", storeList);
		model.addAttribute("genreList", genreList);

		return "books/books";

	}

	@PostMapping("/addBook")
	@ResponseBody
	public int addBook(@RequestBody BookDTO bookDTO) {
		log.info("Received bookDTO: genreId={}", bookDTO.getGenreId());
		booksService.insertBooks(bookDTO);
		return 1;
	}

}
