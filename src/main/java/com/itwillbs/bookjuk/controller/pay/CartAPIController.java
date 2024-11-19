package com.itwillbs.bookjuk.controller.pay;

import com.itwillbs.bookjuk.service.pay.CartService;
import com.itwillbs.bookjuk.service.userPage.UserPageService;
import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CartAPIController {


    private final CartService cartService;
    private final UserPageService userPageService;

    @PostMapping("/rentDetail")
    @ResponseBody
    public Map<String, String> cartBookRent(HttpSession session){
        ArrayList<Long> resultArr = (ArrayList<Long>) session.getAttribute("myCartBookId");
        Long myPoint = Long.valueOf(userPageService.getUserPoint(SecurityUtil.getUserNum()));

        boolean isResult = cartService.myCartBooksRent(resultArr, myPoint);
        if (isResult){
            session.removeAttribute("myCartBookId");
            return Map.of("response", "success");
        }
        return Map.of("response", "fail");
    }





}
