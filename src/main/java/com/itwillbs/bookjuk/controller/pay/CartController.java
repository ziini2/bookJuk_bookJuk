//package com.itwillbs.bookjuk.controller.pay;
//
//import com.itwillbs.bookjuk.service.pay.CartService;
//import com.itwillbs.bookjuk.util.SecurityUtil;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import jakarta.servlet.http.HttpSession;
//
//@Controller
//public class CartController {
//
//    // 장바구니 페이지를 보여주는 메소드 (GET 요청)
//    @GetMapping("/cart")
//    public String viewCart(HttpSession session, Model model) {
//        // 로그인된 유저 번호
//        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기
//
//        if (user != null) {
//            // 세션에서 로그인한 사용자별 장바구니 가져오기
//            CartService cartService = (CartService) session.getAttribute("cart-" + user);
//            if (cartService == null) {
//                cartService = new CartService();  // 장바구니가 없으면 새로 생성
//                session.setAttribute("cart-" + user, cartService);
//            }
//
//            model.addAttribute("cart", cartService.getItems());
//        } else {
//            // 로그인하지 않은 경우 처리
//            model.addAttribute("message", "로그인 후 이용해주세요.");
//        }
//
//        return "pay/cart";  // pay 폴더 내의 cart.html 파일을 렌더링
//    }
//
//    // 장바구니에 상품 추가 (POST 요청)
//    @PostMapping("/cart/add")
//    public String addToCart(@RequestParam("productId") Long productId,
//                            @RequestParam("quantity") int quantity,
//                            HttpSession session) {
//        // 로그인된 유저 번호
//        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기
//
//        if (user != null) {
//            // 세션에서 로그인한 사용자별 장바구니 가져오기
//            CartService cartService = (CartService) session.getAttribute("cart-" + user);
//            if (cartService == null) {
//                cartService = new CartService();  // 장바구니가 없으면 새로 생성
//                session.setAttribute("cart-" + user, cartService);
//            }
//
//            // 상품을 장바구니에 추가
//            cartService.addItem(productId, quantity);
//        } else {
//            // 로그인하지 않은 경우 처리
//            return "redirect:/login";  // 로그인 페이지로 리디렉션
//        }
//
//        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
//    }
//
//    // 장바구니에서 상품 제거 (POST 요청)
//    @PostMapping("/cart/remove")
//    public String removeFromCart(@RequestParam("productId") Long productId, HttpSession session) {
//        // 로그인된 유저 번호
//        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기
//
//        if (user != null) {
//            // 세션에서 로그인한 사용자별 장바구니 가져오기
//            CartService cartService = (CartService) session.getAttribute("cart-" + user);
//            if (cartService != null) {
//                // 상품 제거
//                cartService.removeItem(productId);
//            }
//        }
//
//        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
//    }
//
//    // 장바구니 비우기 (POST 요청)
//    @PostMapping("/cart/clear")
//    public String clearCart(HttpSession session) {
//        // 로그인된 유저 번호
//        Long user = SecurityUtil.getUserNum();  // 로그인된 사용자 ID 가져오기
//
//        if (user != null) {
//            // 세션에서 로그인한 사용자별 장바구니 제거
//            session.removeAttribute("cart-" + user);  // 세션에서 장바구니 제거
//        }
//
//        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
//    }
//}
