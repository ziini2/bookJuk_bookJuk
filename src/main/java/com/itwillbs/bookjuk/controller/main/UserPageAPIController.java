package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.dto.UserPageBooksDTO;
import com.itwillbs.bookjuk.service.userPage.BooksInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class UserPageAPIController {

    private final BooksInfoService booksInfoService;

    @PostMapping("/getBooksInfo")
    @ResponseBody
    public Map<String, Object> getBooksInfo(@RequestParam("booksId") Long booksId){
        //책 상세보기 클릭시 ajax 통신을위한 메서드
        log.info("getBooksInfo");
        UserPageBooksDTO result = booksInfoService.getBookInfo(booksId);
        if (result != null){
            return Map.of("response", "success", "result", result);
        }
        return Map.of("response", "fail");
    }

}
