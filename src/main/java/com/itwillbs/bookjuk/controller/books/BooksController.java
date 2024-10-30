package com.itwillbs.bookjuk.controller.books;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class BooksController {
	
	@GetMapping("/admin/books")
	public String books() {
		log.info("BooksController books()");
		return "/books/books";
	}

	
}
