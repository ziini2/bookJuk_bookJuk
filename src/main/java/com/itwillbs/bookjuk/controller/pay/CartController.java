package com.itwillbs.bookjuk.controller.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.service.pay.CartService;
import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class CartController {

    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/cart")
    public String getCartItems(HttpSession session, Model model) {
        // 현재 로그인한 유저 ID를 가져옵니다.
        Long currentUserNum = SecurityUtil.getUserNum();
        if (currentUserNum == null) {
            // 유저가 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }  

        // 세션에 저장된 유저 ID와 현재 유저 ID 비교
        Long sessionUserNum = (Long) session.getAttribute("userNum");
        if (sessionUserNum == null || !sessionUserNum.equals(currentUserNum)) {
            // 세션 초기화 후 현재 유저 정보 저장
            session.setAttribute("userNum", currentUserNum);
            session.setAttribute("myCartBookId", new ArrayList<>());
        }

        // 장바구니 데이터를 가져옵니다.
        List<BookInfoEntity> bookInfoList = cartService.getUserCartItems(session);

        // Model에 bookInfoList 추가
        model.addAttribute("bookInfoList", bookInfoList);

        // cart.html 뷰로 이동
        return "/pay/cart";
    }

//    @PostMapping("/rent")
//    public ResponseEntity<String> rentBooks(@RequestBody List<Long> bookIds, HttpSession session) {
//        // 로그인된 사용자 정보 가져오기 (세션 또는 SecurityUtil 사용)
//        Long userNum = (Long) session.getAttribute("userNum");
//        if (userNum == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//
//        try {
//            // 대여 처리 요청
//            cartService.rentBooks(bookIds, userNum);
//            return ResponseEntity.ok("대여가 완료되었습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

}

//    // 장바구니 페이지를 표시하는 메서드
//    @GetMapping("/user/cart")
//    public String viewCartPage(HttpSession session, Model model) {
//        // 현재 로그인한 유저 ID를 가져옴 (세션 또는 SecurityUtil을 통해)
//        Long currentUserNum = SecurityUtil.getUserNum();
//
//        // 유저가 로그인하지 않았다면 로그인 페이지로 리다이렉트
//        if (currentUserNum == null) {
//            log.error("User is not logged in. Cannot display cart.");
//            model.addAttribute("message", "You must be logged in to view your cart.");
//            return "redirect:/login";  // 로그인 페이지로 리다이렉트
//        }
//
//        // 세션에서 로그인된 유저의 장바구니 목록 가져오기
//        List<Long> myCartBookIdList = cartService.getCart(currentUserNum, session);
//
//        // 장바구니가 비어있다면 메시지를 추가
//        if (myCartBookIdList == null || myCartBookIdList.isEmpty()) {
//            model.addAttribute("message", "장바구니가 비어 있습니다.");
//        } else {
//            // 세션에서 장바구니에 담긴 책 ID 리스트를 이용해 책 정보 조회
//            List<BookInfoEntity> bookInfoList = cartService.getBooksFromIds(myCartBookIdList);
//
//            // 책 정보가 없을 경우의 처리 (예: 책 정보를 가져오지 못한 경우)
//            if (bookInfoList == null || bookInfoList.isEmpty()) {
//                model.addAttribute("message", "장바구니에 담긴 책 정보를 불러오지 못했습니다.");
//            } else {
//                model.addAttribute("bookInfoList", bookInfoList);
//            }
//        }
//
//        // 장바구니 페이지로 이동
//        return "/pay/cart";  // /pay/cart.html 뷰로 이동
//    }
//    
//    @PostMapping("/rentDetail")
//    public String showRentDetail(@RequestParam("selectedBooks") String selectedBooksJson, Model model) throws Exception {
//        // selectedBooksJson을 JSON 배열로 파싱
//        List<Long> selectedBookIds = new ObjectMapper().readValue(selectedBooksJson, new TypeReference<List<Long>>() {});
//
//        // 해당 bookIds로 책 정보를 가져오기
//        List<BookInfoEntity> selectedBooks = cartService.getBooksFromIds(selectedBookIds);
//      
//        
//        // 총 대여 금액 계산
//        int totalAmount = cartService.calculateTotalAmount(selectedBooks);
//        
//        // 계산된 총액을 모델에 담아서 뷰로 전달
//        model.addAttribute("totalAmount", totalAmount);
//        // 모델에 선택된 책들 추가
//        model.addAttribute("selectedBooks", selectedBooks);
//        // 대여 상세 페이지로 이동
//        return "/pay/pay_detail";  // 대여 상세 페이지 이름
//    }
//
//
//}
