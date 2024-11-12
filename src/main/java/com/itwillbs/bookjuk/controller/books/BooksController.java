package com.itwillbs.bookjuk.controller.books;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.itwillbs.bookjuk.dto.BookDTO;
import com.itwillbs.bookjuk.entity.GenreEntity;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;

import com.itwillbs.bookjuk.service.books.BooksService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.itwillbs.bookjuk.repository.StoreRepository;

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
    	
    	log.info("bookDTO : {}",bookDTO);
    	
    	booksService.insertBooks(bookDTO);
    	
    	return 1;
    	
    }
    
   
}
