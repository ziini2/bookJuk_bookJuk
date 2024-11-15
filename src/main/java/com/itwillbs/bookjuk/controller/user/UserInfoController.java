package com.itwillbs.bookjuk.controller.user;

import com.itwillbs.bookjuk.dto.PaginationDTO;
import com.itwillbs.bookjuk.dto.RentPaginationDTO;
import com.itwillbs.bookjuk.service.user.UserInfoService;
import com.itwillbs.bookjuk.util.PaginationUtil;
import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final int PAGE_SIZE = 10;
    private final int PAGE_BLOCK_SIZE = 10;


    @GetMapping("/info")
    public String userInfo(@RequestParam(name = "rentPage", defaultValue = "0") int rentPage,
//                           @RequestParam(name = "pointPage", defaultValue = "0") int pointPage,
                           Model model) {
        if (SecurityUtil.getUserNum() == null){
            return "redirect:/login";
        }
        log.info("user info");
        //1.유저 정보를 넘겨줘야함
        //2.비밀번호 수정할 수 있어야함
        //3.이용정보 정보 넘겨줘야함(총 대여 도서수, 나의 포인트)
        //간편로그인인지 일반로그인인지 확인도 해서 간편이면 비밀번호 변경버튼도 disable 처리해야한다!
        //아이디도 엄청 길기때문에 loginType만 보여줘야한다!
        model.addAttribute("userInfo", userInfoService.getUserInfo(SecurityUtil.getUserNum()));

        //1.책 대여 내역 넘겨줘야함 (유저 별)
        RentPaginationDTO rentPaginationDTO = userInfoService.getBookRentInfo(SecurityUtil.getUserNum(), PAGE_SIZE, rentPage);
        Map<String, Object> pagination = PaginationUtil.getPagination(rentPaginationDTO.getCurrentPage(), rentPaginationDTO.getTotalPages(), PAGE_BLOCK_SIZE);

        model.addAttribute("bookRentInfo", rentPaginationDTO);
        model.addAttribute("pagination", pagination);
        //2.포인트 거래 내역 넘겨줘야함 (유저 별)
//        model.addAttribute("pointInfo", userInfoService.getPointInfo(SecurityUtil.getUserNum()));


        return "user/userInfo";
    }



}
