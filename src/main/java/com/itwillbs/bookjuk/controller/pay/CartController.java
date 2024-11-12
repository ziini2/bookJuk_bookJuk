package com.itwillbs.bookjuk.controller.pay;

import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.repository.BookInfoRepository;
import com.itwillbs.bookjuk.service.pay.CartService;
import com.itwillbs.bookjuk.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	 @Autowired
	 private BookInfoRepository bookInfoRepository;
	 
    // 장바구니 페이지를 보여주는 메소드 (GET 요청)
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        // 로그인된 유저 번호
        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기

        if (user != null) {
            // 세션에서 로그인한 사용자별 장바구니 가져오기
            CartService cartService = (CartService) session.getAttribute("cart-" + user);
            if (cartService == null) {
                cartService = new CartService();  // 장바구니가 없으면 새로 생성
                session.setAttribute("cart-" + user, cartService);
            }

            Map<Long, Integer> cartItems = cartService.getItems();  // 장바구니의 아이템들

            // 장바구니의 각 아이템에 대해 도서 정보 가져오기
            List<BookInfoEntity> bookInfoList = new ArrayList<>();
            for (Long bookNum : cartItems.keySet()) {
                Optional<BookInfoEntity> bookInfoOpt = bookInfoRepository.findById(bookNum);
                bookInfoOpt.ifPresent(bookInfoList::add);
            }

            // 장바구니 아이템 정보와 책 정보 모델에 추가
            model.addAttribute("cartItems", cartItems);  // 장바구니 아이템
            model.addAttribute("bookInfoList", bookInfoList);  // 책 정보
        } else {
            // 로그인하지 않은 경우 처리
            model.addAttribute("message", "로그인 후 이용해주세요.");
        }

        return "pay/cart";  // pay 폴더 내의 cart.html 파일을 렌더링
    }

    // 장바구니에 상품 추가 (POST 요청)
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("bookNum") Long bookNum,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {
        // 로그인된 유저 번호
        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기

        if (user != null) {
            // 세션에서 로그인한 사용자별 장바구니 가져오기
            CartService cartService = (CartService) session.getAttribute("cart-" + user);
            if (cartService == null) {
                cartService = new CartService();  // 장바구니가 없으면 새로 생성
                session.setAttribute("cart-" + user, cartService);
            }

            try {
                // 상품을 장바구니에 추가
                cartService.addItem(bookNum, quantity);
            } catch (IllegalArgumentException e) {
                // 예외 처리: 재고 부족 등의 문제 발생 시
                return "redirect:/cart?error=" + e.getMessage();  // 에러 메시지를 URL 파라미터로 전달
            }
        } else {
            // 로그인하지 않은 경우 처리
            return "redirect:/login";  // 로그인 페이지로 리디렉션
        }

        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
    }

    // 장바구니에서 상품 제거 (POST 요청)
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("bookNum") Long bookNum, HttpSession session) {
        // 로그인된 유저 번호
        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기

        if (user != null) {
            // 세션에서 로그인한 사용자별 장바구니 가져오기
            CartService cartService = (CartService) session.getAttribute("cart-" + user);
            if (cartService != null) {
                // 상품 제거
                cartService.removeItem(bookNum);
            }
        }

        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
    }

    // 장바구니 비우기 (POST 요청)
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        // 로그인된 유저 번호
        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기

        if (user != null) {
            // 세션에서 로그인한 사용자별 장바구니 제거
            session.removeAttribute("cart-" + user);  // 세션에서 장바구니 제거
        }

        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
    }
}
