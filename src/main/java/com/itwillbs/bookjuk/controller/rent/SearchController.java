package com.itwillbs.bookjuk.controller.rent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.bookjuk.domain.rent.SearchResultDTO;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.service.rent.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SearchController {

    private SearchService searchService;

//    @GetMapping("/admin/search")
//    public List<UserEntity> searchUsers(
//            @RequestParam("criteria") String criteria,
//            @RequestParam("keyword") String keyword) {
//        return searchService.searchUsers(criteria, keyword);
//    }
//
//    @GetMapping("/admin/searchbook")
//    public List<SearchResultDTO> searchBooks(
//            @RequestParam("criteria") String criteria,
//            @RequestParam("keyword") String keyword) {
//        return searchService.searchBooks(criteria, keyword);
//    }
    
}
