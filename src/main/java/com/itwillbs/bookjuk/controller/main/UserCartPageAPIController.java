package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
}
