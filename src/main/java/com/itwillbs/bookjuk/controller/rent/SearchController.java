package com.itwillbs.bookjuk.controller.rent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.service.rent.RentService;

@RestController
public class SearchController {

    @Autowired
    private RentService rentService;

    @GetMapping("/searchMembers")
    public List<UserEntity> searchMembers(@RequestParam String criteria, @RequestParam String keyword) {
        return rentService.searchMembers(criteria, keyword); // JSON 형식으로 검색 결과 반환
    }
}
