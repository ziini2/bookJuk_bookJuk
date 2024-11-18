package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.dto.UserPaginationDTO;
import com.itwillbs.bookjuk.service.userPage.UserPageService;
import com.itwillbs.bookjuk.util.PaginationUtil;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserPageController {
    private final UserPageService userPageService;
    private final int PAGE_SIZE = 10;
    private final int PAGE_BLOCK_SIZE = 10;


    //관리자가 회원페이지로도 이동을 할 수 있게 만든다.
    //이유는 ("/") 하게되면 관리자일때 회원페이지가 아닌 관리자 대시보드 화면으로 넘어가기때문!
    @GetMapping("/")
    public String userCheck() {
        log.info("userMain");
        log.info("userRole: {}", SecurityUtil.getUserRoles());
        log.info("userName: {}", SecurityUtil.getUserName());
        log.info("userNum: {}", SecurityUtil.getUserNum());

        //권한이 "ROLE_INACTIVE"라면 리다이렉트
        if (SecurityUtil.hasRole("ROLE_INACTIVE")){
            return "redirect:/login/phone";
        }
        // 관리자가 로그인했을 때는 관리자 대시보드로 리다이렉트
        else if (SecurityUtil.hasRole("ROLE_ADMIN")){
            //권한이 "ROLE_ADMIN"라면 리다이렉트
            return "redirect:/admin/dashboard";
        }
        else
            return "redirect:/userMain";
    }


    
    // 관리자도 회원페이지로 이동할 수 있게 하는 메서드(버튼클릭시)
    @GetMapping("/userMain")
    public String userMain(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "search", required = false) String keyword,
                           @RequestParam(name = "genre", required = false) String genre,
                           @RequestParam(name = "store", required = false) String store,
                           Model model) {
        //회원 포인트 전달
        addCommonAttributes(model);

        //검색과 장르 필터가 값 유지
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        model.addAttribute("store", store);

        //대여가능한 페이지 List 전달
        UserPaginationDTO userPaginationDTO = userPageService.getBooksList(page, PAGE_SIZE, keyword, genre, store);
        Map<String, Object> pagination = PaginationUtil.getPagination(userPaginationDTO.getCurrentPage(), userPaginationDTO.getTotalPages(), PAGE_BLOCK_SIZE);

        model.addAttribute("booksInfo",userPaginationDTO);
        model.addAttribute("pagination", pagination);

        //전체 모아보기에 보여줄 장르 List
        model.addAttribute("genreList", userPageService.getGenreList());
        //전체 모아보기에 보여줄 지점별 List
        model.addAttribute("storeList", userPageService.getStoreList());

        return "userMain";
    }

    //유저 포인트를 모델에 담아주는 메서드
    private void addCommonAttributes(Model model) {
        // 현재 로그인된 유저의 정보를 모델에 추가
        if (SecurityUtil.getUserNum() != null) {
            model.addAttribute("userPoint", userPageService.getUserPoint(SecurityUtil.getUserNum()));
        }
        model.addAttribute("userName", SecurityUtil.getUserName());
    }
    
    
    
}
