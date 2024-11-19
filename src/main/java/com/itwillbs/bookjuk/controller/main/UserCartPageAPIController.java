package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserCartPageAPIController {

    @PostMapping("/bookCart/add")
    @ResponseBody
    public Map<String, Object> bookCartAdd(@RequestParam("booksId") Long booksId, HttpSession session) {
        log.info("bookCartAdd booksId: {}", booksId);
        //현재 로그인한 유저 ID를 가져옴
        Long currentUserNum = SecurityUtil.getUserNum();

        //세션에서 기존 유저 ID 가져옴
        Long sessionUserNum = (Long) session.getAttribute("userNum");
        if (sessionUserNum == null) {
            //현재 유저 ID 저장
            session.setAttribute("userNum", currentUserNum);
        }
        else if (!sessionUserNum.equals(currentUserNum)) {
            //세션에 저장된 userNum이 없거나, 현재 로그인한 유저와 다르면 세션 초기화
            //세션 초기화
            session.invalidate();
            session.setAttribute("userNum", currentUserNum);
        }

        //세션에서 장바구니 리스트 가져오고 없으면 새로 생성
        ArrayList<Long> myCartBookIdList = (ArrayList<Long>) session.getAttribute("myCartBookId");
        if (myCartBookIdList == null) {
            myCartBookIdList = new ArrayList<>();
        }

        // 중복 방지 후 booksId 추가
        if (!myCartBookIdList.contains(booksId)) {
            myCartBookIdList.add(booksId);
        }

        // 세션에 업데이트된 리스트 저장
        session.setAttribute("myCartBookId", myCartBookIdList);
        log.info("Updated myCartBookIdList: {}", myCartBookIdList);
        return Map.of("response", "success", "myCartBookIdList", myCartBookIdList);
    }
    

    @PostMapping("/bookCart/remove")
    @ResponseBody
    public Map<String, Object> bookCartRemove(@RequestBody Map<String, Object> request, HttpSession session) {
        Long booksId = Long.valueOf(request.get("booksId").toString());  // 책 ID 추출

        // 세션에서 장바구니 리스트 가져오기
        ArrayList<Long> myCartBookIdList = (ArrayList<Long>) session.getAttribute("myCartBookId");
        if (myCartBookIdList == null) {
            myCartBookIdList = new ArrayList<>();
        }

        // 책 ID가 리스트에 존재하면 제거
        boolean removed = myCartBookIdList.remove(booksId);

        // 제거 후 리스트를 세션에 업데이트
        session.setAttribute("myCartBookId", myCartBookIdList);
        
        // 로그로 삭제 여부 확인
        if (removed) {
            log.info("책이 성공적으로 삭제되었습니다. ID: {}", booksId);
        } else {
            log.warn("장바구니에서 해당 책을 찾을 수 없습니다. ID: {}", booksId);
        }

        // 응답 반환
        return Map.of("response", "success", "myCartBookIdList", myCartBookIdList);
    }

  


    // 장바구니 조회
    @GetMapping("/bookCart/view")
    @ResponseBody
    public Map<String, Object> bookCartView(HttpSession session) {
        // 세션에서 장바구니 리스트 가져오기
        ArrayList<Long> myCartBookIdList = (ArrayList<Long>) session.getAttribute("myCartBookId");
        if (myCartBookIdList == null) {
            myCartBookIdList = new ArrayList<>();
        }

        log.info("Current myCartBookIdList: {}", myCartBookIdList);

        return Map.of("response", "success", "myCartBookIdList", myCartBookIdList);
    }

    // 장바구니 초기화 (옵션)
    @PostMapping("/bookCart/clear")
    @ResponseBody
    public Map<String, Object> bookCartClear(HttpSession session) {
        // 세션에서 장바구니 리스트 삭제
        session.removeAttribute("myCartBookId");
        log.info("Cart cleared");

        return Map.of("response", "success", "message", "Cart has been cleared");
    }
}
